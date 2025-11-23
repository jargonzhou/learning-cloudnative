package com.spike.dataengineering.flink.datastream;

import com.google.common.collect.Lists;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.json.JsonDeserializationSchema;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.sink.PrintSink;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.kafka.KafkaContainer;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class FraudDetectionJob {
    private static final Logger LOG = LoggerFactory.getLogger(FraudDetectionJob.class);

    private static final String KAFKA_TOPIC = "txn";

    public static void main(String[] args) throws Exception {
        // setup
        KafkaContainer kafka = new KafkaContainer("apache/kafka:3.7.0");
        setup(kafka);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // source
        JsonDeserializationSchema<Transaction> jsonFormat = new JsonDeserializationSchema<>(Transaction.class);
        KafkaSource<Transaction> source = KafkaSource.<Transaction>builder()
                .setBootstrapServers(kafka.getBootstrapServers())
                .setTopics(KAFKA_TOPIC)
                .setGroupId("fd-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(jsonFormat)
                .build();
        WatermarkStrategy<Transaction> watermarkStrategy = WatermarkStrategy.noWatermarks();
        DataStream<Transaction> transactions = env
                .fromSource(source, watermarkStrategy, "txnSource");

        // fraud detection
        DataStream<Alert> alerts = transactions.keyBy(Transaction::getAccountId) // by each account
                .process(new FraudDetector())
                .name("fraud-detector");

        // sink
        // output to console
        alerts.sinkTo(new PrintSink<>()).name("send-alters");

        // execute
        env.execute("Fraud Detection");

        // clean up
        kafka.stop();
    }

    private static void setup(KafkaContainer kafka) {
        kafka.start();

        while (!kafka.isRunning()) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        LOG.info("Kafka started.");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Transaction> transactions = Lists.newArrayList(
                        new Transaction("1", 13.01),
                        new Transaction("1", 25.00),
                        new Transaction("1", 0.09),
                        new Transaction("1", 510.00),
                        new Transaction("1", 102.62),
                        new Transaction("1", 91.50),
                        new Transaction("1", 0.02),
                        new Transaction("1", 30.01),
                        new Transaction("1", 701.83),
                        new Transaction("1", 31.92));

                ObjectMapper objectMapper = new ObjectMapper();
                Properties kafkaProps = new Properties();
                kafkaProps.put("bootstrap.servers", kafka.getBootstrapServers());
                kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
                kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

                try (KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProps)) {
                    while (true) {
                        for (Transaction transaction : transactions) {
                            String value = objectMapper.writeValueAsString(transaction);
                            ProducerRecord<String, String> record =
                                    new ProducerRecord<>(KAFKA_TOPIC, value); // no key
                            RecordMetadata sendResult = producer.send(record).get();
                            System.err.println(sendResult + ": " + value);

                            // mock delay
                            Thread.sleep(2000L);
                        }
                    }
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();
    }


    public static class Transaction implements Serializable {
        private String accountId;
        private double amount;

        public Transaction() {
        }

        public Transaction(String accountId, double amount) {
            this.accountId = accountId;
            this.amount = amount;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getAccountId() {
            return accountId;
        }

        public double getAmount() {
            return amount;
        }
    }

    public static class Alert {
        private String id;

        public Alert() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Alert{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }

    public static class FraudDetector extends KeyedProcessFunction<String, Transaction, Alert> {

        private static final double SMALL_AMOUNT = 1.00;
        private static final double LARGE_AMOUNT = 500.00;
        private static final long ONE_MINUTE = 60 * 1000;

        // 状态
        private transient ValueState<Boolean> flagState;
        private transient ValueState<Long> timerState;

        @Override
        public void open(OpenContext openContext) throws Exception { // RichFunction
            // whether meet small amount txn
            ValueStateDescriptor<Boolean> flagStateDescriptor = new ValueStateDescriptor<>(
                    "flag",
                    Types.BOOLEAN);
            flagState = this.getRuntimeContext().getState(flagStateDescriptor);
            // 1 minute timeout for fraud detection
            ValueStateDescriptor<Long> timerStateDescriptor = new ValueStateDescriptor<>(
                    "timer-state",
                    Types.LONG);
            timerState = this.getRuntimeContext().getState(timerStateDescriptor);
        }

        @Override
        public void processElement(Transaction value,
                                   KeyedProcessFunction<String, Transaction, Alert>.Context ctx,
                                   Collector<Alert> out) throws Exception {
            Boolean lastTransactionWasSmall = flagState.value(); // read state
            if (lastTransactionWasSmall != null) {
                // found: small amount txn followed by large amount txn
                // output alert
                if (value.getAmount() > LARGE_AMOUNT) {
                    Alert alert = new Alert();
                    alert.setId(value.getAccountId());
                    out.collect(alert);
                }

                this.cleanUp(ctx);
            }

            // meet small amount txn
            if (value.getAmount() < SMALL_AMOUNT) {
                flagState.update(true); // update state

                // register processing time based timer
                long timer = ctx.timerService().currentProcessingTime() + ONE_MINUTE;
                ctx.timerService().registerProcessingTimeTimer(timer);
                timerState.update(timer);
            }
        }

        // called when timer fires
        @Override
        public void onTimer(long timestamp,
                            KeyedProcessFunction<String, Transaction, Alert>.OnTimerContext ctx,
                            Collector<Alert> out) throws Exception {
            // clean up state
            timerState.clear();
            flagState.clear();
        }

        private void cleanUp(KeyedProcessFunction<String, Transaction, Alert>.Context ctx) throws IOException {
            Long timer = timerState.value();
            ctx.timerService().deleteProcessingTimeTimer(timer); // delete timer

            // clean up state
            timerState.clear();
            flagState.clear();
        }
    }
}

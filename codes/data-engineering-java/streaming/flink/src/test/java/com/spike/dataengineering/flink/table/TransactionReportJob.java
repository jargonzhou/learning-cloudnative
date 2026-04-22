package com.spike.dataengineering.flink.table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Lists;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.PrintSink;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.Tumble;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.expressions.TimeIntervalUnit;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.kafka.KafkaContainer;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.lit;

public class TransactionReportJob {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionReportJob.class);

    private static final String KAFKA_TOPIC = "transactions";

    public static void main(String[] args) throws Exception {
        // setup
        KafkaContainer kafka = new KafkaContainer("apache/kafka:3.7.0");
        setup(kafka);

        MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.36")
                .withDatabaseName("sql-demo")
                .withUsername("sql-demo")
                .withPassword("demo-sql")
                .withInitScript("mysql-init-spend_report.sql");
        setup(mysql);

        // table environment
//        EnvironmentSettings settings = EnvironmentSettings.inStreamingMode();
//        TableEnvironment env = TableEnvironment.create(settings);
        StreamExecutionEnvironment senv = StreamExecutionEnvironment.createLocalEnvironment();
        StreamTableEnvironment env = StreamTableEnvironment.create(senv);

        // register tables: Kafka, MySQL
        final String kafkaTableSql = "CREATE TABLE transactions (\n" +
                "    account_id  BIGINT,\n" +
                "    amount      BIGINT,\n" +
                "    transaction_time TIMESTAMP(3),\n" +
                "    WATERMARK FOR transaction_time AS transaction_time - INTERVAL '5' SECOND\n" + // watermark
                ") WITH (\n" +
                "    'connector' = 'kafka',\n" +
                "    'topic'     = '" + KAFKA_TOPIC + "',\n" +
                "    'properties.bootstrap.servers' = '" + kafka.getBootstrapServers() + "',\n" +
                "    'properties.group.id' = 'report-group',\n" +
                "    'scan.startup.mode' = 'earliest-offset',\n" +
                "    'value.format'    = 'json',\n" +
                "    'value.json.ignore-parse-errors' = 'false'\n" +
                ")";
        LOG.info(kafkaTableSql);
        TableResult kafkaTableResult = env.executeSql(kafkaTableSql);
        LOG.info("kafkaTableResult: {}", kafkaTableResult);

        TableResult mysqlTableResult = env.executeSql("CREATE TABLE spend_report (\n" +
                "    account_id BIGINT,\n" +
                "    log_ts     TIMESTAMP(3),\n" +
                "    amount     BIGINT\n," +
                "    PRIMARY KEY (account_id, log_ts) NOT ENFORCED" +
                ") WITH (\n" +
                "   'connector'  = 'jdbc',\n" +
                "   'url'        = '" + mysql.getJdbcUrl() + "',\n" +
                "   'table-name' = 'spend_report',\n" +
                "   'driver'     = '" + mysql.getDriverClassName() + "',\n" +
                "   'username'   = '" + mysql.getUsername() + "',\n" +
                "   'password'   = '" + mysql.getPassword() + "'\n" +
                ")");
        LOG.info("mysqlTableResult: {}", mysqlTableResult);

        env.executeSql("CREATE TABLE print_spend_report (\n" +
                "    account_id BIGINT,\n" +
                "    log_ts     TIMESTAMP(3),\n" +
                "    amount     BIGINT\n" +
                ") WITH (\n" +
                "   'connector'  = 'print'\n" +
                ")");
        // queries
        Table transactions = env.from("transactions");
        // env.toDataStream(transactions).sinkTo(new PrintSink<>()); // DEBUG

        Table spendReport = report(transactions);
        spendReport.printExplain();
        spendReport.executeInsert("print_spend_report");

        spendReport.executeInsert("spend_report");
        env.toDataStream(spendReport).sinkTo(new PrintSink<>()); // DEBUG
        senv.execute();
    }

    public static class Transaction implements Serializable {
        @JsonProperty("account_id")
        private Long accountId;
        @JsonProperty("amount")
        private Long amount;
        @JsonProperty("transaction_time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS")
        private LocalDateTime transactionTime;

        public Transaction() {
        }

        public Transaction(Long accountId, Long amount) {
            this.accountId = accountId;
            this.amount = amount;
        }

        public Long getAccountId() {
            return accountId;
        }

        public void setAccountId(Long accountId) {
            this.accountId = accountId;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public LocalDateTime getTransactionTime() {
            return transactionTime;
        }

        public void setTransactionTime(LocalDateTime transactionTime) {
            this.transactionTime = transactionTime;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "accountId=" + accountId +
                    ", amount=" + amount +
                    ", transactionTime=" + transactionTime +
                    '}';
        }
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

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 120 * 1000); // default: 120s
        AdminClient adminClient = KafkaAdminClient.create(props);
        try {
            adminClient.createTopics(Collections.singleton(new NewTopic(KAFKA_TOPIC, 3, (short) 1))).all().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Transaction> transactions = Lists.newArrayList(
                        new Transaction(1L, 1301L),
                        new Transaction(1L, 2500L),
                        new Transaction(1L, 9L),
                        new Transaction(1L, 51000L),
                        new Transaction(1L, 10262L),
                        new Transaction(1L, 9150L),
                        new Transaction(1L, 2L),
                        new Transaction(1L, 3001L),
                        new Transaction(1L, 70183L),
                        new Transaction(1L, 3192L));

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                Properties kafkaProps = new Properties();
                kafkaProps.put("bootstrap.servers", kafka.getBootstrapServers());
                kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
                kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

                try (KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProps)) {
                    while (true) { // death loop
                        for (Transaction transaction : transactions) {
                            transaction.setTransactionTime(LocalDateTime.now());

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

    private static void setup(MySQLContainer<?> mysql) {
        mysql.start();

        while (!mysql.isRunning()) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        LOG.info("MySQL started");

        Thread t = new Thread(new DumpMySQL(mysql.getDriverClassName(),
                mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword()));
        t.start();
    }

    private static Table report(Table transactions) {
        // lit: literal
        // $: projection
        // call: call UDF
        return transactions
                .select($("account_id"),
                        $("transaction_time").floor(TimeIntervalUnit.MINUTE).as("log_ts"),
//                        call(MyFloor.class, $("transaction_time")).as("log_ts"), // UDF
                        $("amount"))
                .groupBy($("account_id"), $("log_ts"))
                .select($("account_id"),
                        $("log_ts"),
                        $("amount").sum().as("amount"));
    }

    private static Table reportWindow(Table transactions) {
        // call: call UDF
        return transactions
                .window(Tumble.over(lit(1).minute()).on($("transaction_time")).as("log_ts"))
                .groupBy($("account_id"), $("log_ts"))
                .select($("account_id"),
                        $("log_ts").start().as("log_ts"),
                        $("amount").sum().as("amount"));
    }

    // UDF
    public static class MyFloor extends ScalarFunction {
        public @DataTypeHint("TIMESTAMP(3)") LocalDateTime eval(
                @DataTypeHint("TIMESTAMP(3)") LocalDateTime timestamp) {
            return timestamp.truncatedTo(ChronoUnit.HOURS);
        }
    }

    public static class DumpMySQL implements Runnable {
        private Connection connection;
        private Statement statement;

        public DumpMySQL(String driverClassName, String jdbcUrl, String username, String password) {
            try {
                Class.forName(driverClassName);

                this.connection = DriverManager.getConnection(jdbcUrl, username, password);
                this.statement = connection.createStatement();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(30000); // 30 seconds
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                try (ResultSet rs = statement.executeQuery("SELECT * FROM spend_report " +
                        "ORDER BY account_id ASC, log_ts DESC " +
                        "LIMIT 3");) {
                    while (rs.next()) {
                        long accountId = rs.getLong(1);
                        Timestamp logTs = rs.getTimestamp(2);
                        long amount = rs.getLong(3);
                        System.err.println("MySQL: " + accountId + "\t" + logTs + "\t" + amount);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Test
    public void json() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        Transaction transaction = new Transaction(1L, 1301L);
        transaction.setTransactionTime(LocalDateTime.now());
        String value = objectMapper.writeValueAsString(transaction);
        System.err.println(value);


        Transaction transactionDes = objectMapper.readValue(value, Transaction.class);
        System.err.println(transactionDes);

    }
}

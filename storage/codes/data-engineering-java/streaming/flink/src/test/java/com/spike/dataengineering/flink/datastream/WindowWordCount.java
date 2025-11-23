package com.spike.dataengineering.flink.datastream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

// example: https://nightlies.apache.org/flink/flink-docs-release-1.20/docs/dev/datastream/overview/
// logging: https://nightlies.apache.org/flink/flink-docs-release-1.20/docs/deployment/advanced/logging/
public class WindowWordCount {
    private static final Logger LOG = LoggerFactory.getLogger(WindowWordCount.class);

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        LOG.info("Env: {}", env);

        DataStream<Tuple2<String, Integer>> dataStream = env
                // test with: nc -lk 9999
                .socketTextStream("localhost", 9999)
                .flatMap(new Splitter())
                .keyBy(value -> value.f0)
                .window(TumblingProcessingTimeWindows.of(Duration.ofSeconds(5)))
                .sum(1);

        dataStream.print();

        LOG.info("Execute");
        env.execute("Window WordCount");
    }

    public static class Splitter implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String line, Collector<Tuple2<String, Integer>> collector) throws Exception {
            for (String word : line.split(" ")) {
                collector.collect(new Tuple2<>(word, 1));
            }
        }
    }
}

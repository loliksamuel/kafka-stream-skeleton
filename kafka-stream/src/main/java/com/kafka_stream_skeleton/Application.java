package com.kafka_stream_skeleton;

import com.kafka_stream_skeleton.model.LoginCount;
import com.kafka_stream_skeleton.model.LoginData;
import com.kafka_stream_skeleton.serialization.SerdeBuilder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Application {

    private static final String TOPIC_IN          = System.getenv("INPUT_TOPIC");
    private static final String TOPIC_OUT         = System.getenv("OUTPUT_TOPIC");
    private static final String APPLICATION_ID    = System.getenv("APPLICATION_ID");
    private static final String BOOTSTRAP_SERVER  = System.getenv("KAFKA_URL");
    private static final String WINDOW_SIZE_MS    = System.getenv("WINDOW_SIZE_MS");
    private static final String WINDOW_MOVE_MS    = System.getenv("WINDOW_MOVE_MS");

    public static void main(final String[] args) {


        final KafkaStreams streams = buildStream();
        streams.cleanUp();
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }


    private static KafkaStreams buildStream() {

        final Serde<String>     serdeKey = Serdes.String();
        final Serde<LoginData>  serdeVal = SerdeBuilder.buildSerde(LoginData.class);
        final Serde<LoginCount> serdeOut = SerdeBuilder.buildSerde(LoginCount.class);

        final StreamsBuilder builder = new StreamsBuilder();

        final KStream<String, LoginData> source = builder.stream(TOPIC_IN, Consumed.with(Serdes.String(), serdeVal));
              KTable <Windowed<String>, Long> dest;

        System.out.println("start stream process on topic "+TOPIC_IN);

        dest = source.filter((key, value) -> value != null)
                     .map   ((key, value) -> new KeyValue<>(value.getUserName(), value))
                     .groupByKey(Serialized.with(serdeKey, serdeVal))
                     .windowedBy(TimeWindows.of       (Long.parseLong(WINDOW_SIZE_MS))
                                            .advanceBy(Long.parseLong(WINDOW_MOVE_MS)))
                     .count();

        dest.toStream()
              .map((windowed,count)->new KeyValue<>(windowed.key(),new LoginCount(windowed.key(),count,windowed.window().start(),windowed.window().end())))
              .to (TOPIC_OUT, Produced.with(serdeKey, serdeOut));

        System.out.println("streaming processing will be produced to topic "+TOPIC_OUT);

        return new KafkaStreams(builder.build(), getProperties());
    }

    private static Properties getProperties() {
        final Properties streamsConfiguration = getConfig();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        return streamsConfiguration;
    }

    private static Properties getConfig() {
        return new Properties();
    }

}
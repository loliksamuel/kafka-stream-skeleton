package com.kafka_stream_skeleton.consumer;

import com.kafka_stream_skeleton.model.LoginCount;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

public class Application {
    private final static String TOPIC = System.getenv("OUTPUT_TOPIC");
    public static final java.lang.String CONSUMER_GROUP = System.getenv("CONSUMER_GROUP");

    public static void main(String[] args) {
        System.out.println("starting kafka consumer");
        String kafkaUrl = System.getenv("KAFKA_URL");
        System.out.println(String.format("kafka url: %s", kafkaUrl));
        System.out.println(String.format("kafka topic: %s", TOPIC));

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaUrl);


        props.put("group.id", CONSUMER_GROUP);

        /*
         *  this should match the kafka_stream_skeleton output key-value types
         */
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", "com.kafka_stream_skeleton.consumer.serialization.JsonPOJODeserializer");

        KafkaConsumer<String, LoginCount> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Arrays.asList(TOPIC));

        boolean running = true;

        try {
            while (running) {
                ConsumerRecords<String, LoginCount> records = consumer.poll(1000);
                for (ConsumerRecord<String, LoginCount> record : records) {
                    System.out.println(String.format("MESSAGE=> key:%s, value:%s",  record.key(), record.value()));
                }
            }
        } catch (Exception e){
            throw  new RuntimeException(e);
        }
            finally
         {
            System.out.println("close consumer");
            consumer.close();
        }

    }
}

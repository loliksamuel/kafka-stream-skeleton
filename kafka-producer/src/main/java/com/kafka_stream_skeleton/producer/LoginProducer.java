package com.kafka_stream_skeleton.producer;

import com.kafka_stream_skeleton.model.LoginData;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;


import java.util.Properties;

public class LoginProducer {


    private Producer producer;

    private Producer<String, LoginData> getProducer(String topic) {
        String kafkaUrl = System.getenv("KAFKA_URL");

        if(kafkaUrl==null){
            throw new RuntimeException("kafka url must be given");
        }
        System.out.println("start produce data to kafka "+kafkaUrl + " to topic " +topic);


        if (producer == null) {
            Properties configProperties = new Properties();
            configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG     , kafkaUrl);
            configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG  , "org.apache.kafka.common.serialization.StringSerializer");
            configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "com.kafka_stream_skeleton.producer.serialization.JsonPOJOSerializer");

            producer = new KafkaProducer<String, String>(configProperties);
        }

        return producer;

    }


    public void produce(String topicName, String userName, String userPassword, String ip, Long date) {

        LoginData loginData = new LoginData(userName, userPassword, ip, date);

        System.out.println("custom : "+userName + " --> ([ '" + userName      + "' | '"
                                                                  + userPassword  + "' | '"
                                                                  + ip            + "' | "
                                                                  + date          + " ])");
        //user_4 --> ([ 'user_4' | 'password_16' | 3.28.16.084' | 1545503298870 ]) ts:15
        ProducerRecord<String, LoginData> rec = new ProducerRecord<>(topicName, loginData);

        getProducer(topicName).send(rec);
    }

}

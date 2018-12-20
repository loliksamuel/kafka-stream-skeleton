package com.kafka_stream_skeleton.producer;

import java.util.Date;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        String topic = System.getenv("INPUT_TOPIC");
        System.out.println(topic);
        LoginProducer loginProducer = new LoginProducer();

        while (true) {
            Thread.sleep(3000);
            loginProducer.produce(topic, "userNamefromProducer", "pwd_bla", "ip_bla", new Date().getTime());
        }
    }
}

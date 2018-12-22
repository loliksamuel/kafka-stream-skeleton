package com.kafka_stream_skeleton.producer;

import java.util.Date;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        String topic = System.getenv("INPUT_TOPIC");
        System.out.println(topic);
        LoginProducer loginProducer = new LoginProducer();
        int i=10;
        while (true) {
            Thread.sleep(1000);
            i++;
            loginProducer.produce(topic, "user"+ i, "password" + i, i+"."+(i/2)+"."+i+"."+(i*2), new Date().getTime());

            if (i > 15)
                i = 10;
        }
    }
}

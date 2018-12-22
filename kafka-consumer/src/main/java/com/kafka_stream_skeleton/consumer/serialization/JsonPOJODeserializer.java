package com.kafka_stream_skeleton.consumer.serialization;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka_stream_skeleton.model.LoginCount;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class JsonPOJODeserializer implements Deserializer<LoginCount> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public LoginCount deserialize(String topic, byte[] data) {
        if (data == null)
            return null;

        LoginCount loginCount;
        try {
            loginCount = objectMapper.readValue(data, LoginCount.class);
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return loginCount;
    }


    @Override
    public void close() {
    }

}
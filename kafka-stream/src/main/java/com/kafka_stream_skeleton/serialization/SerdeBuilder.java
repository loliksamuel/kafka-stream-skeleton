package com.kafka_stream_skeleton.serialization;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.util.HashMap;
import java.util.Map;

public class SerdeBuilder {

    public static <T> Serde<T> buildSerde(Class<T> clazz) {
        Map<String, Object> serdeProps = new HashMap<>();
        final Serializer<T> jsonPOJOSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", clazz);
        jsonPOJOSerializer.configure(serdeProps, false);

        final Deserializer<T> jsonPOJODeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", clazz);
        jsonPOJODeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(jsonPOJOSerializer, jsonPOJODeserializer);
    }
}

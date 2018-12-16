# kafka-stream-skeleton
This skeleton contains kafka consumer, stream processor and consumer that just print out the stream output.
all are run with docker compose.

There is a module contains the data model used for producer and stream.
 
## installation

1. run mvn clean install
2. add .env file contains your IP, for example:
```properties
LOCALHOST_IP=192.168.2.100
```
3. docker-compose up -d --build

now you have 5 images up and running:
1. kafka
2. zookeeper
3. kafka-producer
4. kafka-stream
5. kafka-consumer

check logs of the consumer `docker logs kafka-consumer -f` to see the stream output
check logs of the consumer `docker logs kafka-producer -f` to see the producer data ( actually used for stream input)
  
## Modification

1. if you want use the built in kafka-connect you can remove the producer image from docker-compose
2. if you change the input type of the producer, you may change this also in the stream input Serdes:
```java
final KStream<String, LoginData> source = builder.stream(INPUT_TOPIC, 
                                                        Consumed.with(Serdes.String(), loginDataSerde));

```
3. if the stream processor writes a different types to the output topic, you may specify the new Serdes when writing the result to the output topic

```java
        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        counts.toStream((windowed, count) ->
                "user:" + windowed.key() + ":" + windowed.toString())
                .to(OUTPUT_TOPIC, Produced.with(stringSerde, longSerde));
```

This changes also need to be applied in the consumer, that consume the stream output

```java
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", LongDeserializer.class.getName());
        
        KafkaConsumer<String, Long> consumer = new KafkaConsumer<>(props);

```
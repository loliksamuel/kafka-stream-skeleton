# kafka-stream-skeleton

<p>Kafka stream started project</p>
<ul>
  <li><a href="#overview">Overview</a></li>
  <li><a href="#mock-data">Fill input topic with mock data</a></li>
  <li><a href="#Stream-processing">Stream processing</a></li>
  <li><a href="#Consuming-stream-data">Consuming stream data</a></li>
  <li><a href="#installation">Installation</a></li>
</ul>

## Overview

This skeleton contains stream processor and consumer that just print out the stream output.
There is a module contains the data model used for the stream.
In order, to fill the stream input topic with data, you can use data generation tool, or build the producer image.


## mock data

1.1 By data generation
Confluent has great [tool](https://docs.confluent.io/current/ksql/docs/tutorials/generate-custom-test-data.html) to generate data by avro schema.

check also [GitHub page](https://github.com/confluentinc/ksql/tree/master/ksql-examples)

This tool is used by default to fill stream input topic with data.

schema must be located in <root_directory>/schema.

current schema is model.avro that has same types has the model used for stream processing.

schema is used in docker-compose file, in image `datagen`

```yaml
  datagen:
    image: confluentinc/ksql-examples:latest
    container_name: datagen
    volumes:
    - ./schema:/schema
    command: "bash -c 'ksql-datagen \
                      schema=./schema/model.avro \
                      key=userName \
                          format=json \
                          topic=users-data \
                          maxInterval=1000 \
                          bootstrap-server=kafka:9092 \
                          propertiesFile=./schema/datagen.properties'"
```

if you will want to change the schema name, just change the value of attribute in schema, in the bash command.
also, you may need also to change the key attribute, with the key in your new model.

schema can be very strong tool, check more examples [here](https://github.com/confluentinc/ksql/tree/master/ksql-examples)  
 
1.2 By custom producer
data can be also produced in the old and known way, by using KafkaProducer class. see module producer.

you need to specify the key and value serializations, there are defaults for primitives like String, int, long, byte array. for json we need to write our own class. 

1.3 you can also run some kafka connect to some external source, there is no example here. need to add your own image for it
```java
configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "com.kafka_stream_skeleton.producer.serialization.JsonPOJOSerializer");
KafkaConsumer<String, Long> consumer = new KafkaConsumer<>(props);
```

if using this option, you need to uncomment this in the docker-compose file, and comment the datagen image 
 
## Stream processing

Stream processing is done in stream module, Application class, stream read the input topic data, and do dome grouping and aggregation.
stream must define SerDes (Serialization and Deserialization) for key and value, this also need to be defined if grouping/counting/aggregation methods change the key/value type.

```java
Serde<LoginData> loginDataSerde = SerdeBuilder.buildSerde(LoginData.class);

final KStream<String, LoginData> source = builder.stream(INPUT_TOPIC, 
                                           Consumed.with(Serdes.String(), loginDataSerde));

```

and because the counting method change the types, I must specify this:
```java
final Serde<String> stringSerde = Serdes.String();
final Serde<Long> longSerde = Serdes.Long();

counts.toStream((windowed, count) ->
        "user:" + windowed.key() + ":" + windowed.toString())
        .to(OUTPUT_TOPIC, Produced.with(stringSerde, longSerde));
```

Also here there are default SerDes for primitive types, and for json need to write our own SerDes.
use the class com.kafka_stream_skeleton.serialization.SerdeBuilder, to create a custom SerDes.

## Consuming stream data

Stream output data exists in its own topic and need to be consumed, I write some naive consumer, that just print result to the console.
also here need to specify correctly the serializers, according to the stream results
```java
props.put("key.deserializer", StringDeserializer.class.getName());
props.put("value.deserializer", LongDeserializer.class.getName());
    
KafkaConsumer<String, Long> consumer = new KafkaConsumer<>(props);
    
consumer.subscribe(Arrays.asList(TOPIC));
```

to see output:
```
docker logs kafka-consumer -f
```

also here, you can use some kafka sink connect, to send result to some external system, DB, elasticsearch

## Installation

1. run mvn clean install
2. add .env file contains your IP, for example:
```properties
LOCALHOST_IP=192.168.2.100
```
3. docker-compose up -d --build

now you have 5 images up and running:
    1. kafka
    2. zookeeper
    3. datagen (or kafka-producer)
    4. kafka-stream
    5. kafka-consumer

4. docker-compose stop 
    to stop all images
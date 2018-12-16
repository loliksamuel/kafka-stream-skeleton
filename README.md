# kafka-stream-skeleton
This skeleton contains kafka consumer, stream processor and consumer that just print out the stream output.
all are run with docker compose.

## installation

1. run mvn clean install
2. docker-compose up -d --build

now you have 5 images up and running:
1. kafka
2. zookeeper
3. kafka-producer
4. kafka-stream
5. kafka-consumer

check logs of the consumer `docker logs kafka-consumer -f` to see the stream output
check logs of the consumer `docker logs kafka-producer -f` to see the producer data ( actually used for stream input)
  

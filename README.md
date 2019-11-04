# Kafka topic cleaner
The goal is to clean up unused topics in Kafka

Unused topics are topics that match the following three conditions:
- Not `internal` that used by kafka components.
- Topics have not been consumed for a while, that the consumers are already cleaned up by kafka,
which by default is 7 days.
- Topic that is empty

## Environment variables
- `BOOTSTRAP_SERVERS`: broker hosts
- `IS_DELETE_ENABLED`: without setting this, this application does not delete any topic and only log the topics should be deleted

## How to use

#### Pull from dockerhub
`docker run --rm --name kafka-topic-cleaner --network=host -e BOOTSTRAP_SERVERS=localhost:29092 sendohchange/kafka-topic-cleaner`

### Build locally
- `mvn clean package`
 - `docker build -t kafka-topic-cleaner .` 
 - `docker run --rm --name --network=host kafka-topic-cleaner -e BOOTSTRAP_SERVERS=localhost:29092 kafka-topic-cleaner` 
 or
 - `docker run --rm --name --network=host kafka-topic-cleaner -e BOOTSTRAP_SERVERS=localhost:29092 -e IS_DELETE_ENABLED=true kafka-topic-cleaner`

## How to test locally
Use `docker-compose` and it creates some sample topics that demonstrates deletions  

## Why not python
To know which topics are/were consumed, the library must implement ListConsumerGroup API.
Only one python library is up-to-date,
developed by confluent, but this API does not implement ListConsumerGroup API yet.
https://github.com/confluentinc/confluent-kafka-python/issues/223

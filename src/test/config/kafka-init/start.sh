#!/bin/sh

# Waiting for Kafka
while ! nc -z kafka 9092; do
  sleep 1
done

# Create target topics

kafka-topics.sh --zookeeper zookeeper:2181 --create --if-not-exists --replication-factor 1 --partitions 3 --topic test123 --config cleanup.policy=compact

kafka-topics.sh --zookeeper zookeeper:2181 --create --if-not-exists --replication-factor 1 --partitions 3 --topic __schemas

kafka-topics.sh --zookeeper zookeeper:2181 --create --if-not-exists --replication-factor 1 --partitions 3 --topic payments

echo "test 1" | kafka-console-producer.sh --broker-list kafka:9092 --topic payments
echo "test 2" | kafka-console-producer.sh --broker-list kafka:9092 --topic payments

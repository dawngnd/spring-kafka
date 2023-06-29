# spring-kafka
## using docker compose to build kafka server
- docker-compose up -d
- go to kafka UI : localhost:8080 to create and view topics
- alternative using command line to create topic: kafka-topics.sh --bootstrap-server localhost:9092 --topic MessagingQueue2 --create --partitions 3 --replication-factor 1
- [kafka topic config document](https://docs.confluent.io/platform/current/installation/configuration/topic-configs.html)

## Start spring project

## Send message with:
* curl -X POST "localhost:8003/messages" -H "Content-Type:application/json" -d '{"times":"5","key":"key1","samecontent":"false"}'


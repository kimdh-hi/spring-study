
---

```
docker exec -it kafka bash 

kafka-topics --bootstrap-server kafka:29092 --create --if-not-exists --topic input-topic --replication-factor 1 --partitions 1

kafka-console-producer --bootstrap-server kafka:29092 --topic input-topic
```
## Spring kafka


```
# zookeeper start
bin/zookeeper-server-start.sh config/zookeeper.properties
```

```
# kafka start
bin/kafka-server-start.sh config/server.properties
```

```
# topic 생성
bin/kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092

# topic 목록확인
./bin/kafka-topics.sh --bootstrap-server=localhost:9092 --list
```



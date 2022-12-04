

#### Zookeeper
3.6.3 <br/>
https://zookeeper.apache.org/releases.html

```bash
bin/zkServer.sh start-foreground
```

#### Kafka
2.8.0 Scala 2.13 <br/>
https://kafka.apache.org/downloads

```bash
bin/kafka-server-start.sh config/server.properties
```

```bash
토픽 생성
bin/kafka-topics.sh --create --topic [topic-name] --bootstrap-server localhost:9092 

생성 확인
bin/kafka-topics.sh --list --zookeeper localhost:2181
bin/kafka-topics.sh --bootstrap-server=localhost:9092 --list
```




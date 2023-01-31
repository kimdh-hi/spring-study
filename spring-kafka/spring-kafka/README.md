## Spring kafka


```
# zookeeper start
bin/zkServer.sh start-foreground

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

---

KafkaTemplate
- 기본적으로 비동기로 처리
  - 동기처리도 가능
- `Message<?>`, `ProducerRecord<K,V` 이용 가능

RoutingKafkaTemplate
- spring kafka 2.5 이상
- 토필별로 옵션을 다르게 설정 가능 (토픽명의 경우 정규식 표현 지원)

ReplyingKafkaTemplate
- spring kafka 2.1.3 이상
- Consumer 가 특정 데이터를 받았는지 여부 확인 가능
- 3개 헤더 정의
  - `KafkaHeaders.CORRELATION_ID`: 요청과 응답을 연결 (필수)
  - `KafkaHeaders.REPLY_TOPIC`: 응답토픽 (필수)
  - `KafkaHeaders.REPLY_PARTITION`: 응답 토픽의 파티션 (선택)

AggregatingReplyingKafkaTemplate
- 여러 응답을 한 번에 처리
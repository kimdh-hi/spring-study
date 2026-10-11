# Spring Stomp Broker Relay

## overview

```
// ws1, ws2
client --SEND--> ws1 --SEND(system conn)--> rabbitmq(amq.topic, routing key: rooms.{roomId})
rabbitmq --MESSAGE(client conn)--> ws1/ws2 --> client
```

- 구독/분배는 RabbitMQ STOMP 가 담당, 서버는 relay 만 수행
- destination 은 RabbitMQ routing key 규칙에 맞춰 `.` 구분 (`/topic/rooms.{roomId}`)
- 중복 세션 종료는 브로커가 대신할 수 없어 AMQP fanout exchange(노드별 anonymous queue)로 노드 간 전파

## run

```shell
docker run -d --name rabbitmq -p 5672:5672 -p 61613:61613 rabbitmq:4.1-alpine \
  sh -c 'echo "[rabbitmq_stomp]." > /etc/rabbitmq/enabled_plugins && exec docker-entrypoint.sh rabbitmq-server'
./gradlew bootRun
```

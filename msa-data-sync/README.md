# msa-data-sync

MSA 서비스 간 데이터 싱크 샘플 — **Transactional Outbox + Debezium CDC + Kafka + MySQL**

## 문제

- 서비스가 "비즈니스 DB 저장"과 "이벤트 발행"을 동시에 해야 하는데, DB 트랜잭션과 Kafka 발행은 한 단위로 묶이지 않는다 (**dual-write 문제**). 저장 후 발행 직전 장애 시 데이터 불일치 발생.

## 해결 구조

```
[order-service]  --(1 tx: orders + outbox INSERT)-->  MySQL(order_db)
                                                          | binlog (ROW)
                                                          v
                                               [Debezium Connect]
                                           Outbox Event Router SMT
                                                          | publish
                                                          v
                                          Kafka topic: datasync.event.Order
                                                          |
                                                          v
[inventory-service] --(@KafkaListener)--> MySQL(inventory_db) 재고 반영
```

- producer는 비즈니스 row와 outbox row를 **같은 로컬 트랜잭션**으로 저장만 한다. Kafka를 직접 호출하지 않음 → order-service에 Kafka 의존성 없음.
- Debezium이 MySQL binlog의 outbox 테이블 변경을 캡처 → Outbox Event Router SMT가 토픽으로 라우팅.
- consumer는 토픽을 구독해 자기 DB에 반영. `orderId` 기준 멱등 처리(at-least-once 대응).

## 구성

| 모듈/컨테이너 | 설명 | 포트 |
|---|---|---|
| order-service | 주문 생성 + outbox 적재 (producer) | 8080 |
| inventory-service | 이벤트 소비 + 재고 반영 (consumer) | 8081 |
| mysql 8.4 | order_db / inventory_db (binlog ROW) | 3306 |
| kafka 3.9 | KRaft 단일 노드 (Zookeeper 없음) | 29092 |
| debezium-connect 3.1 | Kafka Connect + MySQL 커넥터 + Outbox Router | 8083 |
| kafka-ui | 토픽/메시지/커넥터 상태 확인 | 8090 |

## 실행

```bash
# 1. 인프라 기동
docker compose up -d

# 2. producer 기동 (outbox 테이블 생성됨)
./gradlew :order-service:bootRun

# 3. Debezium 커넥터 등록
curl -X POST -H "Content-Type: application/json" \
  --data @debezium/register-outbox.json \
  http://localhost:8083/connectors
# 상태 확인
curl http://localhost:8083/connectors/order-outbox-connector/status

# 4. consumer 기동 (별도 터미널)
./gradlew :inventory-service:bootRun

# 5. 주문 생성
curl -X POST localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"product":"keyboard","amount":3}'

# 6. 싱크 확인
curl localhost:8081/api/inventory          # keyboard 재고 차감 확인
# kafka-ui: http://localhost:8090           # datasync.event.Order 토픽 메시지 확인
```

- consumer를 내린 상태로 주문을 여러 건 생성한 뒤 재기동하면, 밀린 이벤트가 모두 소비됨(at-least-once).

## 대안

- CDC 인프라 없이 앱 내부로 끝내려면 **Spring Modulith Event Externalization**(이벤트 발행 레지스트리=내장 아웃박스)로 동일 보장 가능. 단 언어 무관·진정한 cross-service CDC에는 Debezium 방식이 표준.

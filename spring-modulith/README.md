# spring-modulith

## @ApplicationModuleListener

- async, new transaction, after commit 조합 meta annotation

```
@Async
@Transactional(propagation = Propagation.REQUIRES_NEW)
@TransactionalEventListener
@Documented
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationModuleListener
```

## @ApplicationModuleTest

- 해당 테스트 클래스가 속한 패키지를 포함하는 모듈 내 bean 만 scan 대상으로 취급
- ApplicationModuleTest 의 verifyAutomatically 설정시 타모듈 접근, 순환참조 등 검증
  - default: true

## Modularity test
- ModularityTests.kt (sample)
- 특정 모듈에 대한 테스트가 존재한다면 ApplicationModuleListener 에 의해 모듈 간 참조 등 검증됨.
  - 단, 해당 모듈이하 테스트가 없는 경우 테스트 불가.
  - 검증누락 안전장치로써 `modules.verify()` 사용 가능
- 모듈 간 의존 문서화 기능 제공 (console, puml, adoc..)


## Event Externalization (`@Externalized`)

- module 간 event 를 외부화
  - kafka, amqp, jms ...
- 기존 내부 event 처리 구조에서 producer 측은 @Externalized 추가만으로 이벤트 외부화 가능

```
implementation("org.springframework.modulith:spring-modulith-events-kafka")
```

```kotlin
@Externalized("order.completed::#{orderId}")
data class OrderCompletedEvent(
  val orderId: String,
  val product: String,
)

```

## Event Publication Registry

- 트랜잭션 완료 이후 이벤트 발행 전 이벤트를 발행하지 못한 경우 이벤트 메세지 저장 후 서버 재기동시 재발송
- `spring.modulith.events.completion-mode`
  - `update`: 완료 레코드 유지 (default)
  - `delete`: 완료 즉시 삭제
  - `archive`: 별도 테이블로 이관
- `spring.modulith.events.staleness.*`
  - 기본 0이라 꺼져 있음, 설정 안 하면 브로커 장애가 계속돼도 자동으로 `FAILED` 처리 안 됨
  - `published`/`processing`/`resubmitted` 별로 기간 지나면 `FAILED` 전환, `FailedEventPublications.resubmit(...)`로 복구
- `spring.modulith.events.republish-outstanding-events-on-restart`: 재시작 시 미완료 발행 재시도
- registry schema

```sql
CREATE TABLE IF NOT EXISTS EVENT_PUBLICATION
(
  ID                     UUID NOT NULL,
  COMPLETION_DATE        TIMESTAMP(9) WITH TIME ZONE,
  EVENT_TYPE             VARCHAR(512) NOT NULL,
  LISTENER_ID            VARCHAR(512) NOT NULL,
  PUBLICATION_DATE       TIMESTAMP(9) WITH TIME ZONE NOT NULL,
  SERIALIZED_EVENT       VARCHAR(4000) NOT NULL,
  STATUS                 VARCHAR(20),          -- ← 여기
  COMPLETION_ATTEMPTS    INT,                  -- 재시도 횟수
  LAST_RESUBMISSION_DATE TIMESTAMP(9) WITH TIME ZONE,  -- 마지막 재제출 시각
  PRIMARY KEY (ID)
);
```

```yaml
spring:
  modulith:
    events:
      republish-outstanding-events-on-restart: true
      completion-mode: delete
      staleness:
        published: 1m #저장됐지만 리스너가 안 집어감 → 1분 후 실패 처리
        processing: 1m #리스너가 실행 시작했으나 안 끝남(hang/crash) → 1분 후 실패 처리
        resubmitted: 5m #재제출됐으나 또 안 끝남 → 5분 후 실패 처리
```

```
implementation("org.springframework.modulith:spring-modulith-starter-jpa")
```

- https://docs.spring.io/spring-modulith/reference/appendix.html

## event 순서 보장

- `spring.modulith.events.externalization.serialize-externalization` 기본값 false
- 이벤트는 기본적으로 비동기이므로 순서보장 안됨
- A 서비스에 두 개 트랜잭션이 있고 각각 transactional event 발행이 필요한 경우 트랜잭션이 더 빨리 끝난 이벤트가 더 늦게 발행될수도 있음
- 순서보장 관련 이슈: https://github.com/spring-projects/spring-modulith/issues/415

```yaml
spring:
  modulith: 
    events:
      externalization:
        serialize-externalization: true
```




## 참고

- https://docs.spring.io/spring-modulith/reference/index.html
- https://docs.spring.io/spring-modulith/reference/fundamentals.html
- https://docs.spring.io/spring-modulith/reference/testing.html

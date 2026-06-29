# readOnly 마킹 메커니즘

> 검증 환경: Spring Framework 7.0.8 / Hibernate 7.4.1 / Spring Boot 4.1.0

두 방식은 `@Transactional(readOnly=true)`를 **서로 다른 레이어**에서 읽는다.

| 방식 | 무엇을 보고 라우팅 | 동작 보장 |
|---|---|---|
| Replication Driver | JDBC 커넥션 `setReadOnly(true)` | 조건부 |
| RoutingDataSource | 트랜잭션 메타데이터 (ThreadLocal) | 항상 |

## RoutingDataSource — 항상 동작

- 트랜잭션 매니저가 종류와 무관하게 readOnly 값을 ThreadLocal에 기록
  ```java
  // AbstractPlatformTransactionManager.java:581
  TransactionSynchronizationManager.setCurrentTransactionReadOnly(definition.isReadOnly());
  ```
- `isCurrentTransactionReadOnly()`가 이 값을 읽음
- DB·드라이버·커넥션 상태와 무관하게 결정적으로 동작

### LazyConnectionDataSourceProxy가 필수

- 트랜잭션 시작 시 **커넥션 획득이 readOnly 기록보다 먼저** 일어남
  ```java
  doBegin(transaction, definition);            // 커넥션 획득
  prepareSynchronization(status, definition);  // → setCurrentTransactionReadOnly (이후)
  ```
- eager 획득이면 라우팅 시점에 `isCurrentTransactionReadOnly()=false`라 무조건 master로 감
- `LazyConnectionDataSourceProxy`가 첫 SQL까지 커넥션 획득을 미뤄, readOnly 기록 후 라우팅되도록 보장

## Replication Driver — 조건부 동작

- `Connection.setReadOnly(true)` 호출에 의존하는데, JPA 기본 환경에선 이 호출이 일어나지 않음
- `HibernateJpaDialect`는 release mode가 `ON_CLOSE`일 때만 커넥션을 마킹
  ```java
  // HibernateJpaDialect.java:133
  if (this.prepareConnection && ConnectionReleaseMode.ON_CLOSE.equals(...releaseMode))
      DataSourceUtils.prepareConnectionForTransaction(preparedCon, definition);
  ```
- Hibernate 5.2+ 기본 release mode는 `AFTER_TRANSACTION` (Spring Boot도 바꾸지 않음)
- **⇒ 기본 Spring Boot + JPA + Hikari에서 `@Transactional(readOnly=true)`는 `setReadOnly`를 호출하지 않아 읽기가 replica로 안 가고 master로 감**
- JPA는 대신 `FlushMode.MANUAL` + `session.setDefaultReadOnly`만 적용 — 커넥션이 아닌 Hibernate Session 차원

### 동작시키려면

```properties
spring.jpa.properties.hibernate.connection.handling_mode=DELAYED_ACQUISITION_AND_HOLD
```

- release mode가 `ON_CLOSE`가 되어 커넥션 마킹 분기를 탐

## 공통 주의점

- readOnly는 **커넥션을 소유한 트랜잭션** 기준 → write 트랜잭션 안에서 readOnly 메서드를 호출하면 둘 다 master로 감
- `@Transactional` 밖의 쿼리는 readOnly=false 취급 → master
- JPA에서 readOnly=true면 FlushMode.MANUAL이라, 그 안에서 엔티티를 수정해도 flush 없이 조용히 무시됨

## 결론

- **JPA 기반에서는 RoutingDataSource가 더 안전하고 예측 가능**
- Replication Driver는 JPA 기본 환경에서 별도 설정 없이는 라우팅 자체가 동작하지 않음

## 참고 자료

- [#13599 — readOnly transaction doesn't work with JPA](https://github.com/spring-projects/spring-framework/issues/13599) — Replication Driver 한계의 직접 근거. release mode가 `setReadOnly` 적용 여부를 가름
- [#21415 — LazyConnectionDataSourceProxy setup for routing datasource](https://github.com/spring-projects/spring-framework/issues/21415) — RoutingDataSource에서 LazyProxy가 필수인 직접 근거
- [Read-write and read-only transaction routing with Spring — Vlad Mihalcea](https://vladmihalcea.com/read-write-read-only-transaction-routing-spring/) — RoutingDataSource + LazyProxy 정석
- [kwon37xi/replication-datasource](https://github.com/kwon37xi/replication-datasource) — 실전 검증된 구현체

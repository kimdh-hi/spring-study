# readOnly 마킹 메커니즘

> 검증 환경: Spring Framework 7.0.8 / Hibernate 7.4.1 / Spring Boot 4.1.0

- `@Transactional(readOnly=true)`를 두 방식이 **다른 레이어**에서 읽음

| 방식 | 무엇을 보고 라우팅 | 핵심 클래스 |
|---|---|---|
| Replication Driver | JDBC 커넥션 `setReadOnly(true)` | `HibernateJpaVendorAdapter` |
| RoutingDataSource | 트랜잭션 메타데이터 (ThreadLocal) | `LazyConnectionDataSourceProxy` |

## Replication Driver

- `setReadOnly(true)`가 불려야 replica로 감
- `HibernateJpaDialect`는 release mode가 `ON_CLOSE`일 때만 호출
- `HibernateJpaVendorAdapter`가 `ON_CLOSE`를 기본 강제 → 표준 Spring Boot면 **설정 없이 동작**
- 안 되는 경우: `prepareConnection=false` / JTA / 순수 Hibernate → 그때만 `handling_mode` 직접 지정

## RoutingDataSource

- `determineCurrentLookupKey()`가 `isCurrentTransactionReadOnly()`를 읽어 WRITE/READ 결정
- DB·드라이버와 무관하게 항상 동작
- ⚠️ **`LazyConnectionDataSourceProxy` 필수**
  - 커넥션 획득(`doBegin`)이 readOnly 기록보다 먼저 → eager면 무조건 master
  - LazyProxy가 첫 SQL까지 획득을 미뤄 해결

## 공통 주의점

- readOnly는 커넥션을 소유한 트랜잭션 기준 → write tx 안 readOnly 메서드는 둘 다 master
- `@Transactional` 밖 쿼리 → readOnly=false → master
- readOnly=true면 `FlushMode.MANUAL` → 엔티티 수정해도 조용히 무시

## 결론

- 표준 Spring Boot + JPA면 **두 방식 다 설정 없이 동작**
- 차이는 안전성이 아니라 **제어·관찰성**
  - RoutingDataSource: 라우팅 기준 직접 지정·로그 관찰 (LazyProxy 필수)
  - Replication Driver: 드라이버 내부라 관찰·제어 어려움, `prepareConnection`/JTA에 의존

## 참고 자료

- [#21415](https://github.com/spring-projects/spring-framework/issues/21415) — RoutingDataSource에 `LazyConnectionDataSourceProxy`가 필요한 직접 근거 (Spring 공식 회피책)
- [#30969](https://github.com/spring-projects/spring-framework/issues/30969) — 지연 획득 RoutingDataSource 내장 제안, declined → 직접 wiring 필요
- [#13599](https://github.com/spring-projects/spring-framework/issues/13599) — release mode가 `setReadOnly` 적용을 가름 (이후 vendor adapter가 `ON_CLOSE`로 덮어씀)

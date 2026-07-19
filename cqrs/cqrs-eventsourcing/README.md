# CQRS + Event Sourcing

> 명령(쓰기)과 조회(읽기)의 책임을 분리하는 CQRS + 상태를 이벤트의 연속으로 저장하는 Event Sourcing을 **함께** 사용하는 학습용 최소 예제(Spring Boot + JPA + H2 + Kotlin).
> 두 패턴은 별개지만 궁합이 좋다 — Event Sourcing이 쓰기(원천), CQRS 읽기 모델(projection)이 조회를 담당.

## 1. 정의

- CQRS의 핵심: **정보를 갱신할 때 쓰는 모델과 읽을 때 쓰는 모델을 분리**한다. 하나의 개념 모델을 Command(갱신)와 Query(조회)로 나눈다. — [Martin Fowler, "CQRS"](https://martinfowler.com/bliki/CQRS.html)
- 마이크로소프트 정의: CQRS는 **쓰기 연산(command)과 읽기 연산(query)을 분리**하는 패턴. 쓰기 모델은 검증·도메인 로직·트랜잭션 정합성에 최적화하고, 읽기 모델은 표현 계층용 DTO/프로젝션 조회에 최적화한다. — [Microsoft, "CQRS pattern"](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)

## 2. 뿌리: CQS

- CQRS는 Bertrand Meyer의 **CQS(Command Query Separation)** 원칙에 기반. 모든 메서드는 상태를 바꾸는 **Command** 이거나 값을 반환하는 **Query** 중 하나여야 하며 둘을 겸하지 않는다. — [Martin Fowler, "CommandQuerySeparation"](https://martinfowler.com/bliki/CommandQuerySeparation.html)
- CQS가 "메서드" 수준 원칙이라면, CQRS는 이를 **모델·객체 수준으로 확장**한 것. Greg Young이 제시하고 Udi Dahan 등이 발전. — [Microsoft, "Applying simplified CQRS and DDD patterns"](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/apply-simplified-microservice-cqrs-ddd-patterns)

## 3. 왜 분리하나

- 복잡한 도메인에서 하나의 모델로 읽기·쓰기를 모두 처리하면 모델이 비대해지고 **둘 다 제대로 못한다**. 분리하면 각 측을 독립적으로 최적화·확장 가능. — [Fowler](https://martinfowler.com/bliki/CQRS.html)
- 쓰기 모델은 정규화·불변식(invariant) 중심, 읽기 모델은 조인 없는 비정규화 뷰로 **조회 성능**을 높인다. — [Microsoft](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)

## 4. 수준(단계)

- **단순형**: 단일 데이터베이스 + 두 개의 논리 모델. 쿼리 계층과 커맨드 계층만 분리. — [Microsoft .NET 가이드](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/apply-simplified-microservice-cqrs-ddd-patterns)
- **고급형**: 읽기/쓰기 **물리 저장소 분리**. 각각 다른 저장 기술·확장 전략 사용. 저장소 간 동기화는 이벤트로. — [Microsoft](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)
- **이벤트 소싱 결합**: 현재 상태 대신 상태 변경 **이벤트의 연속**을 저장. CQRS와 자주 함께 쓰이지만 별개 패턴. — [Microsoft, "Event Sourcing"](https://learn.microsoft.com/en-us/azure/architecture/patterns/event-sourcing) · [Fowler, "Event Sourcing"](https://martinfowler.com/eaaDev/EventSourcing.html)

## 5. 트레이드오프

- **주의**: 대부분의 시스템에서 CQRS는 **위험한 복잡성**을 더한다. 하나의 바운디드 컨텍스트 안에서도 일부 영역에만 선택적으로 적용하는 것이 맞다. — [Fowler](https://martinfowler.com/bliki/CQRS.html)
- 물리 저장소를 분리하면 읽기 모델은 **최종적 일관성(eventual consistency)** 을 가진다 — 쓰기 반영에 지연이 생긴다. — [Microsoft](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)

---

## 이 예제에서의 구현

게시글 도메인. **Article은 이벤트 소싱**(현재 상태 테이블 없음, 이벤트 스트림이 원천). 작성자는 별도 도메인 없이 `X-User-Id` 헤더로 받은 userId를 이벤트에 기록. 읽기는 CQRS projection.

- 쓰기(ES): `Article` aggregate는 JPA 엔티티가 아니라 이벤트를 재생해 복원. 명령 → 새 이벤트 발생 → `event_store` append(원천)
- 읽기(CQRS): `ArticleSummary` (비정규화 단일 테이블, authorId·commentCount·viewCount 스냅샷) — 같은 이벤트로 갱신
- 저장소: append-only `stored_event`(이벤트 로그) + `article_summary`(읽기 모델) 2개뿐

```
Command → Article(replay로 복원) → 새 이벤트 → EventStore.append
                                                     ├→ stored_event INSERT (원천, append-only)
                                                     └→ publish → @EventListener(Projector) → ArticleSummary(읽기 모델)
```

| 이벤트 | 트리거 | event_store | 읽기 모델 반영 |
|---|---|---|---|
| ArticleWritten | 글 작성 | seq 0 append | summary INSERT |
| ArticleEdited | 글 수정 | seq n append | title UPDATE |
| CommentAdded | 댓글 추가 | seq n append | commentCount +1 |
| ArticleViewed | 조회 | seq n append | viewCount +1 |

- 상태 변경은 모두 이벤트 append로만 발생 — 현재 상태를 직접 UPDATE하지 않음(ES 원칙)
- aggregate id는 UUID(앱에서 생성) — 현재 상태 테이블 없이 id 할당
- 명령 처리 시 `EventStore.load` → `Article.replay(events)` 로 상태 복원 후 새 이벤트 발생

패키지: `presentation/{command,query}` · `application/{command,query}` + `ArticleSummaryProjector` · `domain/{model,repository}` · `infra/eventstore`(`EventStore`)

### 실행

```bash
./gradlew bootRun          # http://localhost:8080
./gradlew test             # 이벤트 append → 재생 복원 → 읽기 모델 반영 검증
```

- `requests.http` 로 시나리오 수동 확인 (`GET /articles/{id}/events` 로 이벤트 스트림 확인)
- H2 콘솔 http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:cqrses`) — `stored_event`(이벤트 로그) vs `article_summary`(읽기 모델) 분리 확인

### 현재 예제의 한계와 확장

- `@EventListener` 동기 처리 → append와 projection이 같은 트랜잭션에서 원자 커밋(즉시 일관성). 물리 분리 시 최종적 일관성(5절)
- 명령마다 전체 이벤트 재생 → 스트림이 길어지면 **스냅샷**으로 재생 비용 절감
- 동시성 제어 없음(seq = 현재 이벤트 수) → 낙관적 락(expectedVersion)으로 동시 수정 충돌 감지
- 확장: `@TransactionalEventListener(AFTER_COMMIT)` + `@Async` → 별도 read DB로 4단계 "고급형" 물리 분리

## 참고 자료

- [Martin Fowler — CQRS](https://martinfowler.com/bliki/CQRS.html)
- [Martin Fowler — Command Query Separation](https://martinfowler.com/bliki/CommandQuerySeparation.html)
- [Martin Fowler — Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html)
- [Microsoft — CQRS pattern (Azure Architecture Center)](https://learn.microsoft.com/en-us/azure/architecture/patterns/cqrs)
- [Microsoft — Event Sourcing pattern](https://learn.microsoft.com/en-us/azure/architecture/patterns/event-sourcing)
- [Microsoft — Applying simplified CQRS and DDD patterns (.NET microservices)](https://learn.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/apply-simplified-microservice-cqrs-ddd-patterns)

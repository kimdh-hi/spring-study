# concurrency

재고 차감(lost update) 시나리오로 동시성 제어 기법을 비교하는 예제.

`Spring Boot 4.1.0` · `Kotlin 2.3.21` · `Java 25` · `Spring Data JPA` · `H2`

---

## 1. 문제

### 1-1. 무엇이 깨지는가

- 재고 차감은 `조회 → 계산 → 저장`의 read-modify-write
- 세 단계가 한 트랜잭션 안에 있어도, 조회한 값을 애플리케이션에서 계산하는 동안 DB는 그 행을 잠그지 않음.

```
시각   트랜잭션 A         트랜잭션 B        DB
 1     조회 → 100                          100
 2                       조회 → 100        100     ← A가 아직 저장 전이라 같은 값을 읽음
 3     저장(99)                             99
 4                       저장(99)           99     ← 2번 차감했는데 1만 감소
```

- **DB는 이상을 감지하지 못함** — 두 UPDATE 문 각각은 정상이며, 낡은 값을 근거로 썼다는 사실은 DB가 알 수 없음.
- **행 락의 유효 구간** — UPDATE 실행 순간에만 걸렸다가 커밋 시 풀리므로 조회~저장 사이의 공백은 보호되지 않음.
- **실측** — 재고 100에 100개 동시 차감 요청 시 잔여 재고 90~96, 요청의 90% 이상 유실.

### 1-2. 격리 수준으로는 못 막는다

- 격리 수준은 **읽기**가 무엇을 보는지를 규정
- 두 트랜잭션이 같은 값을 읽고 각자 쓰는 것 자체는 막지 못함.

| 격리 수준 | 동작 | 결과 |
|---|---|---|
| READ COMMITTED | 커밋된 값만 읽음 | 커밋 전 값을 못 볼 뿐이라 lost update 그대로 발생 |
| REPEATABLE READ | 트랜잭션 내내 같은 스냅샷을 읽음 | 스냅샷이 낡은 값을 고정해 상황 악화 |
| SERIALIZABLE | 직렬 실행과 동등하게 강제 | 막아주지만 트랜잭션 전체를 직렬화해 비용 과다 |

- **결론** — lost update는 격리 수준이 아니라 **갱신 전략**으로 풀어야 하는 문제.

### 1-3. 해결 원리

| - | 아이디어 | 포기하는 것 | 해당 기법 |
|---|---|---|---|
| **직렬화** | 임계 구역을 한 번에 하나만 통과시킴 | 처리량 (대기 발생) | synchronized, 비관적 락, Named Lock, 분산 락 |
| **충돌 감지** | 일단 진행하고 충돌하면 재시도 | 예측 가능성 (재시도 횟수) | 낙관적 락 |
| **원자화** | read-modify-write를 한 연산으로 축약 | 표현력 (도메인 로직 제약) | 조건부 UPDATE, 파티션 직렬화 |

---

## 2. 해결 기법

### 2-1. No Lock — 기준선

아무 보호 장치 없이 `조회 → 차감 → 저장`을 수행하는 대조군.

- **결과** — 100 스레드가 1씩 차감해도 잔여 재고 90~96, 4~10건만 반영.
- **적합** — 동시 갱신이 구조적으로 발생하지 않는 데이터.
- **코드** — `NoLockStockService`.

### 2-2. Application Lock — `synchronized`

JVM 모니터 락으로 임계 구역을 한 스레드씩 통과시키는 방식.

| 장점 | 단점 |
|---|---|
| 구현이 가장 단순, DB·인프라 부하 없음 | 락 객체가 JVM 힙에 있어 인스턴스 2대면 무력화 |
| 별도 인프라 불필요 | 재고 ID와 무관하게 전체 요청이 직렬화되어 처리량 급감 |

**주의** — `@Transactional` 메서드에 `synchronized`를 붙이면 프록시가 트랜잭션을 열고 닫으므로 커밋이 락 **바깥**에서 발생.

```
잘못된 순서                        올바른 순서
─────────────────────             ─────────────────────
[프록시] 트랜잭션 시작              synchronized 획득
  synchronized 획득                  [프록시] 트랜잭션 시작
    조회 → 차감                        조회 → 차감
  synchronized 해제  ← 커밋 전!      [프록시] 커밋
[프록시] 커밋                       synchronized 해제
```

```kotlin
synchronized(lock) {
  noLockStockService.decrease(stockId, quantity)  // 트랜잭션이 이 안에서 시작·커밋
}
```

- **개선** — 재고 ID별 `ConcurrentHashMap<Long, ReentrantLock>`으로 입도를 낮출 수 있으나 단일 JVM 한계는 그대로.
- **적합** — 단일 인스턴스 배치, 운영 스케일아웃에는 부적합.
- **코드** — `SynchronizedStockService`.

### 2-3. Optimistic Lock — `@Version`

- 락을 걸지 않고 진행한 뒤 커밋 시점에 충돌 감지.
- Hibernate가 flush에서 UPDATE를 만들 때 SET 절에 `version+1`, WHERE 절에 조회 시점 version을 넣고, 갱신 행 수가 0이면 예외 발생.

```sql
update optimistic_stock set quantity=?, version=version+1
 where id=? and version=?     -- 갱신 행 0 → StaleObjectStateException
```

| 장점 | 단점 |
|---|---|
| 락 대기·데드락 없음, 읽기 처리량 유지 | 재시도 로직 필수, 실패 처리 책임이 애플리케이션으로 이동 |
| 다중 인스턴스에서 DB 한 곳으로 정합성 보장 | 충돌이 잦으면 재시도 비용이 대기 비용을 넘어서고 기아 발생 |
| 요청 경계를 넘는 편집 충돌도 감지 | 실패한 시도마다 트랜잭션·조회·UPDATE를 소비 |

**동작 주의**

- **예외 계층** — Hibernate `StaleObjectStateException` → JPA `OptimisticLockException` → Spring `ObjectOptimisticLockingFailureException`, 잡을 때는 `OptimisticLockingFailureException` 하나면 충분.
- **감지 시점** — flush 시점에 발생하므로 트랜잭션 메서드 내부 try-catch로는 커밋 단계 예외를 못 잡음.
- **우회 경로** — JPQL 벌크 UPDATE는 version을 증가시키지 않으므로 `version = version + 1`을 직접 명시.

**재시도** — 같은 트랜잭션에서 재시도하면 1차 캐시가 옛 버전 엔티티를 반환해 계속 실패하므로 트랜잭션을 닫고 새로 열어야 함.

```kotlin
// 이 클래스에는 @Transactional 이 없다 — 매 시도마다 새 트랜잭션·새 영속성 컨텍스트
repeat(MAX_ATTEMPTS) {
  try {
    optimisticLockStockService.decrease(stockId, quantity)   // 여기서 트랜잭션 시작·커밋
    return
  } catch (e: OptimisticLockingFailureException) {
    Thread.sleep(BACKOFF_MILLIS)
  }
}
```

- **재시도 경계** — 루프는 트랜잭션 바깥에 위치, 같은 클래스 내부 호출은 프록시를 타지 않아 새 트랜잭션이 열리지 않음.
- **백오프** — 고정 sleep은 thundering herd 유발, 지수 백오프 + 지터 권장.
- **부수 효과** — 재시도 구간의 외부 API 호출·이벤트 발행은 중복 실행되므로 멱등성 필요.
- **대안** — spring-retry `@Retryable(OptimisticLockingFailureException::class)` + 내부 메서드 `@Transactional(REQUIRES_NEW)`.
- **적합** — 충돌 빈도가 낮은 일반 갱신.
- **코드** — `OptimisticLockStockService` + `OptimisticLockRetryService`.

### 2-4. Pessimistic Lock — `SELECT ... FOR UPDATE`

- 조회 시점에 행 배타 락을 잡아 1-1의 공백 구간 제거
- 뒤따르는 트랜잭션은 조회 단계에서 블로킹

```kotlin
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("select s from Stock s where s.id = :id")
fun findByIdForUpdate(@Param("id") id: Long): Stock?   // 락 해제는 트랜잭션 종료 시점
```

| 장점 | 단점 |
|---|---|
| 충돌이 잦아도 재시도 없이 성공 | 대기 시간만큼 처리량 저하, 커넥션 점유 시간 증가 |
| 다중 인스턴스에서 정합성 보장 | 락 획득 순서가 엇갈리면 데드락 (`A→B` vs `B→A`) |
| 재시도가 없어 멱등성 고려 불필요 | 대기 스레드가 커넥션을 쥔 채 쌓여 풀 고갈 위험 |

**락 모드**

| LockModeType | 발행 SQL | 의미 |
|---|---|---|
| `PESSIMISTIC_READ` | `for share` | 공유 락, 읽기 허용·쓰기 차단 |
| `PESSIMISTIC_WRITE` | `for update` | 배타 락, 읽기·쓰기 모두 차단 (기본 선택) |
| `PESSIMISTIC_FORCE_INCREMENT` | `for update` + `version+1` | 배타 락과 함께 버전 강제 증가 |

**동작 주의**

- **최신 값 읽기** — `for update`는 MVCC 스냅샷이 아니라 최신 커밋 값(current read)을 읽으므로 대기 후 재조회 불필요.
- **1차 캐시 함정** — 조회 쿼리가 DB에 나가야 락이 걸리므로, 락 조회를 트랜잭션의 첫 접근으로 배치.
- **없는 행은 못 잠금** — 중복 INSERT 경합에는 무력, 유니크 제약이나 Named Lock(2-6) 필요.
- **인덱스 필수** — InnoDB는 인덱스를 타지 않으면 스캔한 행 전부를 잠가 사실상 테이블 락.

**대응**

- **타임아웃** — `jakarta.persistence.lock.timeout`을 `@QueryHints`로 지정, 실제 상한은 DB 설정(MySQL `innodb_lock_wait_timeout`, 기본 50초).
- **락 순서 고정** — ID 오름차순 등으로 통일해 데드락 사이클 제거.
- **보유 시간 최소화** — 락 조회 이후 외부 API 호출·긴 계산 금지.
- **응용** — `NOWAIT`은 즉시 실패, `SKIP LOCKED`는 잠긴 행을 건너뛰어 작업 큐 폴링에 활용.

- **적합** — 충돌 빈도가 높고 실패를 허용할 수 없는 갱신.
- **코드** — `PessimisticLockStockService`, `StockJpaRepository.findByIdForUpdate`.

> **낙관 vs 비관** — 낙관의 비용은 실패 시도의 재작업, 비관의 비용은 대기. `충돌률 × 재시도 비용`이 대기 시간을 넘어서는 지점이 경계이며, 충돌률을 모르면 낙관으로 시작해 재시도 횟수를 지표로 관찰.

### 2-5. 원자적 조건부 UPDATE

값을 애플리케이션으로 가져오지 않고 DB 안에서 계산. 읽기와 쓰기가 한 문장이라 조회~저장의 공백 없음.

```sql
update stock set quantity = quantity - :qty
 where id = :id and quantity >= :qty     -- 갱신 행 0 → 재고 부족
```

| 장점 | 단점 |
|---|---|
| 락 대기·재시도 없이 가장 빠름 | 도메인 로직이 SQL로 이동해 복잡한 규칙·이벤트 발행 표현이 어려움 |
| 코드가 가장 단순 | 영속성 컨텍스트를 우회하므로 `@Modifying(clearAutomatically = true)` 없으면 이후 조회가 옛 값 반환 |

- **적합** — 카운터·재고처럼 규칙이 단순한 증감 연산.
- **코드** — `AtomicUpdateStockService`, `StockJpaRepository.decreaseIfEnough`.

### 2-6. MySQL Named Lock — `GET_LOCK`

행이 아니라 임의의 문자열 키에 락을 거는 방식. 락 소유 단위는 행이 아니라 **커넥션**.

| 장점 | 단점 |
|---|---|
| 대상 행이 없어도 적용 가능 (중복 생성 방지) | 비즈니스 커넥션으로 잡으면 대기 중 풀 고갈, 락 전용 데이터소스 분리 필요 |
| `GET_LOCK(key, timeout)`으로 무한 대기 회피 | 커넥션이 재사용되므로 해제 누락이 다음 요청의 장애, `finally` 해제 필수 |

- **제외 사유** — H2 미지원.
- **참고** — [`spring-jpa/jpa-named-lock`](../spring-jpa/jpa-named-lock).

### 2-7. 분산 락 — Redis / ZooKeeper

외부 저장소의 키 선점(`SET NX`, Redisson `RLock`)으로 프로세스 경계를 넘어 직렬화.

| 장점 | 단점 |
|---|---|
| DB 부하 없이 애플리케이션 전역 락, TTL로 해제 누락 방어 | 인프라 운영 비용, 락 저장소가 단일 장애점 |
| Redisson은 pub/sub 통지로 스핀락 대비 자원 소모가 적음 | TTL 만료와 처리 지연이 겹치면 두 인스턴스가 동시 보유 가능 |

- **적합** — 여러 서비스·데이터소스에 걸친 임계 구역.
- **참고** — [`spring-cache/redisson-distributed-lock`](../spring-cache/redisson-distributed-lock).

### 2-8. 파티션 직렬화 — Kafka

재고 ID를 메시지 키로 삼으면 같은 키는 같은 파티션으로 라우팅되고, 파티션당 컨슈머가 하나이므로 동일 재고 요청은 순차 처리됨.

| 장점 | 단점 |
|---|---|
| 락이 불필요하고 처리량은 파티션 수로 수평 확장 | 비동기 전환으로 즉시 응답 불가, 실패 보상·결과 통지 설계 필요 |
| 트래픽 급증을 큐가 흡수해 DB 부하 평준화 | 특정 키에 트래픽이 몰리면 핫 파티션 발생 |

- **적합** — 대량 트래픽의 재고·정산 처리.

---

## 3. 비교

| 기법 | 정합성 | 성능 | 유효 범위 | 재시도 | 데드락 | 난이도 |
|---|---|---|---|---|---|---|
| No Lock | ✗ | 최상 | – | – | 없음 | 최하 |
| synchronized | 단일 JVM만 | 하 | 단일 인스턴스 | 불필요 | 없음 | 하 |
| Optimistic Lock | ✓ | 상 | 단일 DB | **필수** | 없음 | 중 |
| Pessimistic Lock | ✓ | 중 | 단일 DB | 불필요 | 있음 | 중 |
| 원자적 UPDATE | ✓ | 최상 | 단일 DB | 불필요 | 없음 | 하 |
| Named Lock | ✓ | 중 | 단일 DB | 불필요 | 있음 | 중 |
| 분산 락 | ✓ | 중 | 전 인스턴스 | 불필요 | 있음 | 상 |
| 파티션 직렬화 | ✓ | 상 | 전 인스턴스 | 불필요 | 없음 | 상 |

- **유효 범위** — 정합성이 보장되는 최대 경계이며, `단일 DB`는 인스턴스를 늘려도 DB가 하나면 안전하다는 의미.
- **성능** — 충돌이 거의 없는 상황 기준이며, 충돌률이 올라가면 낙관적 락이 비관적 락보다 느려지는 역전 발생.

선택 기준은 **충돌 빈도**와 **정합성이 필요한 경계** 두 가지.

- 증감처럼 규칙이 단순하면 원자적 UPDATE를 먼저 검토하고, 표현력이 부족하면 락으로 전환.
- 충돌이 드물면 낙관적 락, 잦으면 비관적 락 — 재시도 비용과 대기 비용의 교환.
- 단일 DB를 벗어나면 분산 락, 응답 지연을 감수할 수 있으면 파티션 직렬화.
- 어느 기법이든 락 범위는 좁게, 보유 시간은 짧게, 외부 API 호출은 락 안에 두지 않음.

---

## 4. 구현 구조

```
stock
├── ui          — StockController, DecreaseStrategy 로 전략 라우팅
├── application — 전략별 차감 서비스 (StockDecreaseService 구현)
├── domain
│   ├── model      — Stock, OptimisticStock
│   └── repository — 저장소 인터페이스 (port)
└── infra       — Spring Data JPA 어댑터
```

- **전략 교체** — 모든 전략이 `StockDecreaseService`를 구현하고, 컨트롤러가 주입받은 구현체 목록을 `strategy`로 매핑해 URL만으로 기법을 교체.
- **포트/어댑터** — `domain.repository`는 순수 인터페이스이고 `infra`가 Spring Data JPA로 구현해, 도메인이 JPA에 의존하지 않음.
- **엔티티 분리** — `@Version`이 붙으면 `NO_LOCK`에서도 낙관적 락이 동작해 lost update 재현이 불가하므로, `Stock`(버전 없음)과 `OptimisticStock`(버전 있음)을 별도 테이블로 분리.

---

## 5. 검증

재고 100을 만든 뒤 100개 스레드가 `CountDownLatch`로 동시에 출발해 1씩 차감. 모든 스레드가 같은 순간에 시작해야 인터리빙이 안정적으로 재현됨 (`StockConcurrencyTest`).

| 전략 | 잔여 재고 | 판정 |
|---|---|---|
| `NO_LOCK` | 90~96 | lost update 재현 |
| `SYNCHRONIZED` | 0 | 정상 |
| `OPTIMISTIC` | 0 | 정상 |
| `PESSIMISTIC` | 0 | 정상 |
| `ATOMIC_UPDATE` | 0 | 정상 |

실제 발행 SQL — 각 기법이 의도한 형태로 나가는지 확인한 결과.

```sql
-- 비관적 락: 조회에 for update 가 붙음
select s1_0.id, s1_0.product_id, s1_0.quantity from stock s1_0 where s1_0.id=? for update

-- 낙관적 락: where 절에 version 조건이 자동 추가됨
update optimistic_stock set product_id=?, quantity=?, version=? where id=? and version=?

-- 원자적 UPDATE: 조회 없이 단일 문장으로 차감
update stock s1_0 set quantity=(s1_0.quantity-cast(? as bigint))
 where s1_0.id=? and s1_0.quantity>=?
```

---

## 6. test

```bash
# 생성 → {"id":1,"quantity":100}
curl -X POST localhost:8080/api/stocks/PESSIMISTIC \
  -H 'Content-Type: application/json' -d '{"productId":1,"quantity":100}'

# 차감 → {"id":1,"quantity":99}
curl -X POST localhost:8080/api/stocks/PESSIMISTIC/1/decrease \
  -H 'Content-Type: application/json' -d '{"quantity":1}'

# 조회
curl localhost:8080/api/stocks/PESSIMISTIC/1
```

- `{strategy}` — `NO_LOCK` · `SYNCHRONIZED` · `OPTIMISTIC` · `PESSIMISTIC` · `ATOMIC_UPDATE`.
- `OPTIMISTIC`은 `optimistic_stock` 테이블을 쓰므로 나머지 전략과 ID 체계가 분리됨.

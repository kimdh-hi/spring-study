# Heap Dump 분석: Spring Boot + JPA + Kotlin 메모리 누수 재현과 MAT 분석

## 힙 기본기

### JVM 힙 구조

- 힙은 크게 **Young 영역**(Eden + Survivor)과 **Old 영역**으로 구분
- 새 객체는 Eden에 할당 → minor GC에서 살아남으면 Survivor → 반복 생존 시 Old로 승격
- 메모리 누수의 전형적 증상: **Old 영역이 Full GC 후에도 줄지 않고 우상향** → 결국 `OutOfMemoryError: Java heap space`
- 힙덤프는 특정 시점의 힙 전체(모든 객체 + 참조 그래프)를 파일(`.hprof`)로 캡처한 것

### GC Roots

- GC가 도달성(reachability) 판단을 시작하는 출발점. GC root에서 도달 가능한 객체는 수거 불가
- 대표 GC root: 실행 중 스레드의 스택 로컬 변수, static 필드, JNI 참조, 모니터 락 잡힌 객체
- 누수 분석의 핵심 질문 = "이 객체가 **어떤 GC root 경로로** 잡혀 있어서 GC가 못 치우나"

### Shallow Heap vs Retained Heap

- **Shallow heap**: 객체 자신이 차지하는 크기 (헤더 + 필드). 참조하는 대상은 미포함
- **Retained heap**: 이 객체가 GC되면 **함께 해제될 모든 객체의 합** — 누수 분석에서 실제로 봐야 하는 값
- 예: `ArrayList` 자체의 shallow heap은 수십 byte지만, 50만 엔티티를 물고 있으면 retained heap은 수백 MB

### Dominator

- 객체 X로 가는 **모든** GC root 경로가 객체 D를 지나면 D가 X의 dominator
- MAT의 Dominator Tree는 이 관계로 힙을 트리화 — "누굴 지우면 뭐가 같이 해제되나"를 바로 보여줌
- retained heap 상위 dominator 몇 개만 보면 힙의 대부분을 설명할 수 있음

---

## 힙덤프 생성 방법 3가지

| 방법 | 시점 | 용도 |
|---|---|---|
| `-XX:+HeapDumpOnOutOfMemoryError` | OOM 발생 순간 자동 | 프로덕션 필수 옵션. 사후 분석 |
| `jcmd` / `jmap` | 원하는 시점 수동 | 점진 누수 관찰 (덤프 2개 비교), 장애 조짐 시 |
| actuator `/actuator/heapdump` | HTTP 요청 시 | 셸 접근 없이 즉시. 노출 시 보안 주의 |

### 1. OOM 시 자동 덤프 (JVM 옵션)

```
-Xmx256m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./dumps
```

- 이 프로젝트의 `bootRun`에 이미 설정됨 (재현 가속을 위해 힙 256m 제한)
- OOM은 재현이 어렵고 재발 대기 비용이 크므로 **프로덕션에서 항상 켜두는 것을 권장**
- 덤프 파일 크기 ≈ 힙 사용량. 디스크 여유 확인 필요

### 2. CLI 수동 덤프 (jcmd 권장)

```bash
jcmd <pid> GC.heap_dump ./dumps/manual.hprof
jmap -dump:live,format=b,file=./dumps/manual.hprof <pid>
```

- `jcmd`가 최신 권장 도구. `jmap`은 레거시지만 여전히 널리 사용
- `live` 옵션: 덤프 전 Full GC 수행 → 살아있는 객체만 캡처. 누수 분석엔 노이즈가 줄어 유리하나 **Full GC로 서비스가 수 초 멈출 수 있음** — 운영 트래픽 중 주의
- pid 확인: `jps -l` 또는 `pgrep -f HeapdumpApplication`

### 3. actuator 엔드포인트

```bash
curl -o heap.hprof localhost:8080/actuator/heapdump
```

- Spring Boot 4 기준 두 가지 설정 필요 — `exposure.include`만으로는 404

```yaml
management:
  endpoints:
    web:
      exposure:
        include: heapdump
  endpoint:
    heapdump:
      access: unrestricted
```
- 힙덤프에는 **메모리 상의 모든 데이터(비밀번호, 세션, 개인정보)가 그대로 들어감** — 외부 노출 절대 금지, 내부망 + 인증 하에서만 개방

### 덤프 전 1차 진단 (덤프 없이 가볍게)

```bash
jcmd <pid> GC.class_histogram | head -20
jmap -histo <pid> | head -20
```

- 클래스별 인스턴스 수/크기 상위 목록만 즉시 확인 — 덤프·분석 비용 없이 용의자 압축
- `char[]`, `byte[]`, `String`이 상위인 것은 정상. **도메인 클래스가 상위에 있으면** 그때 덤프

---

## Eclipse MAT 분석 워크플로

### 설치와 준비

- https://eclipse.dev/mat/ 에서 다운로드
- 대용량 덤프는 MAT 자체 힙이 부족해 파싱 실패 가능 → `MemoryAnalyzer.ini`의 `-Xmx`를 덤프 크기 이상으로 상향
- 덤프 최초 오픈 시 인덱스 생성으로 수 분 소요 (같은 폴더에 인덱스 파일 캐시됨)

### 분석 순서

- **① Leak Suspects Report** — 오픈 시 자동 제안. retained heap 비중이 큰 의심 지점을 자동 요약. 대부분의 단순 누수는 여기서 바로 답이 나옴
- **② Histogram** — 클래스별 인스턴스 수 + shallow/retained heap. `Retained Heap` 컬럼 정렬 후 도메인 패키지(`com.study.*`)로 필터
- **③ Dominator Tree** — retained heap 상위 객체부터 트리 탐색. "무엇이 힙을 지배하나"
- **④ Path to GC Roots** — 용의자 객체 우클릭 → `Path to GC Roots` → `exclude all phantom/weak/soft references`. 캐시성 약참조를 제외하고 **강참조 사슬**만 확인 → 누수 지점 코드 특정
- **⑤ OQL** — SQL 유사 쿼리로 객체 검색

```sql
SELECT * FROM com.study.heapdump.domain.Product
SELECT p.name.toString() FROM com.study.heapdump.domain.Product p
SELECT * FROM java.util.concurrent.ConcurrentHashMap m WHERE m.size > 10000
```

### 점진 누수: 덤프 2개 비교

- 시점 A 덤프 → 부하 지속 → 시점 B 덤프
- MAT에서 B의 Histogram 열기 → `Compare to another Heap Dump` → A 선택
- **인스턴스 수가 계속 증가하는 클래스**가 누수 대상. 단일 덤프로는 "원래 많은 것"과 "새고 있는 것"을 구분 못함

---

## 실습 시나리오

### 공통 준비

```bash
./gradlew bootRun    # -Xmx256m + OOM 시 ./dumps 에 자동 덤프
curl "localhost:8080/seed?count=500000"
```

- `/seed`는 1,000건마다 `flush()` + `clear()` 하는 **올바른 배치 패턴** — 시나리오 2와 대조용

### 시나리오 1 — findAll 대량 로딩

```bash
curl "localhost:8080/leak/find-all"
```

- `repository.findAll()`이 50만 건 전체를 `List<Product>`로 메모리에 적재 → OOM, `./dumps/java_pid*.hprof` 자동 생성
- MAT 관찰 포인트
  - Leak Suspects: 요청 처리 스레드(`http-nio-8080-exec-*`)가 지목됨
  - Dominator Tree: 해당 스레드 → `ArrayList` → `Object[]` → `Product` 다수가 retained heap 대부분 차지
  - GC root가 **스레드 스택**인 것이 특징 — 요청이 끝나면 해제될 메모리지만, 끝나기 전에 힙을 초과
- 교훈: 전체 조회는 페이징(`Pageable`)·스트림(`Stream<T>` + `@Transactional(readOnly)`)·`countBy` 등으로 대체

### 시나리오 2 — 영속성 컨텍스트 비대화

```bash
curl "localhost:8080/leak/persistence-context?count=500000"
```

- 단일 트랜잭션에서 `persist()` 루프, `flush()/clear()` 없음
- 영속성 컨텍스트(1차 캐시)는 **트랜잭션이 끝날 때까지 관리 중인 모든 엔티티 + 스냅샷(dirty checking용 복사본)을 강참조** → 엔티티당 2배 가까운 메모리
- MAT 관찰 포인트
  - Dominator Tree: `StatefulPersistenceContext` → `EntityEntryContext`가 엔티티 전체를 보유
  - Path to GC Roots: 요청 스레드 → 트랜잭션/세션 → 영속성 컨텍스트 사슬 확인
  - Histogram: `Product`와 `EntityEntry`(스냅샷 관련) 인스턴스 수가 나란히 상위
- 교훈: 대량 쓰기는 chunk 단위 `flush()` + `clear()` (`SeedService.seed()` 참고), 또는 JDBC batch/`saveAll` 분할

### 시나리오 3 — static 컬렉션 점진 누수

- 앱 재시작 후 시드는 가볍게 (`/seed?count=100000`) — OOM 없이 점진 증가를 관찰하는 것이 목적

```bash
curl "localhost:8080/seed?count=100000"
for i in $(seq 1 10); do curl -s "localhost:8080/leak/static-cache?count=10000"; echo; done
jcmd $(jps -l | grep HeapdumpApplicationKt | cut -d' ' -f1) GC.heap_dump ./dumps/a.hprof
for i in $(seq 1 5); do curl -s "localhost:8080/leak/static-cache?count=10000"; echo; done
jcmd $(jps -l | grep HeapdumpApplicationKt | cut -d' ' -f1) GC.heap_dump ./dumps/b.hprof
```

- companion object(static)의 `ConcurrentHashMap`에 매 요청 결과를 누적, 제거 없음 — 실무에서 가장 흔한 형태(수동 캐시, 리스너 등록 후 미해제 등)
- 즉시 OOM이 아니라 **호출할수록 Old 영역이 자라는 점진 누수** — 실제 장애와 같은 패턴
- MAT Histogram Compare(b vs a): `Product`, `ConcurrentHashMap$Node` 증가 확인
- `jcmd <pid> GC.class_histogram`만으로도 `Product` 인스턴스 수 증가를 즉시 확인 가능 (실측: 10회 호출 후 상위 5위에 `Product` 10만 개)
- MAT 관찰 포인트
  - Leak Suspects: `LeakController` 클래스의 static 필드(`cache`)를 직접 지목
  - Path to GC Roots: GC root가 **class(static field)** — 시나리오 1(스레드 스택)과의 차이가 핵심. 요청이 끝나도 절대 해제되지 않음
- 교훈: 수동 캐시는 크기 상한/TTL 필수 (Caffeine 등), static 컬렉션에 요청 데이터 축적 금지

---

## JPA 특화 체크포인트

- 영속성 컨텍스트는 트랜잭션 범위 동안 엔티티를 강참조 — "조회만 했는데 메모리가 안 빠진다"의 흔한 원인
- dirty checking 스냅샷 때문에 관리 엔티티는 체감 2배 메모리 — 읽기 전용 대량 조회는 `@Transactional(readOnly = true)` + DTO 프로젝션 권장
- OSIV(`spring.jpa.open-in-view`)가 켜져 있으면 영속성 컨텍스트가 **뷰 렌더링까지 생존** → 유지 시간이 길어짐. 이 프로젝트는 `false`
- 덤프에서 `StatefulPersistenceContext`, `SessionImpl`의 retained heap이 크면 트랜잭션 설계(범위·배치 clear)를 의심

## 한계

- `-Xmx256m`은 재현 가속용 — 실제 서비스 힙에서는 같은 코드도 증상이 늦게 나타남
- H2 파일 모드 단일 인스턴스 기준. 시드 데이터는 재시작 시 `ddl-auto: create`로 초기화
- **OOM 이후의 JVM은 신뢰 불가** — 힙이 한계 근처에 남아 GC 스래싱으로 응답 불능이 될 수 있음(실측). 덤프 확보 후 재시작이 정답. OOM 시나리오(1, 2)는 실행마다 앱 재시작 권장

## 범위 밖

- Hibernate query plan cache 누수 (`IN` 절 파라미터 수 폭발) — `hibernate.query.in_clause_parameter_padding` 참고
- ThreadLocal 누수 (스레드풀 재사용 환경에서 remove 누락)
- GC 로그/JFR 기반 분석 — 힙덤프 이전 단계의 관측
- async-profiler 등 alloc 프로파일링 — "어디서 할당되나"는 덤프가 아닌 프로파일러의 영역

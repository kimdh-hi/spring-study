## Quartz Dynamic Scheduler: 동적 등록 + 멀티 인스턴스 중복 방지

### Quartz 란

- 자바 진영의 스케줄링 라이브러리. `@Scheduled`(단순 고정 스케줄, 코드에 박힘)와 달리 **런타임에 잡을 등록/삭제**하고 **클러스터링**을 지원
- 핵심 구성요소
  - `Scheduler` — 잡·트리거를 등록하고 발사를 관리하는 엔진 (Spring Boot가 빈으로 자동 구성)
  - `Job` — 실제 실행할 로직 (`execute()` 구현). 여기선 `PromptJob`
  - `JobDetail` — 잡 정의 + 파라미터(`JobDataMap`). 프롬프트 문자열을 여기 담아 전달
  - `Trigger` — 언제 발사할지. `CronTrigger`(cron 식) / `SimpleTrigger`(고정 간격)
  - `JobStore` — 잡·트리거 상태 저장소. **RAMJobStore**(메모리, 단일 인스턴스) vs **JDBC JobStore**(DB, 재시작·클러스터 대응)
- 잡·트리거를 DB에 저장하므로 앱을 재시작해도 등록된 스케줄이 유지되고, 여러 인스턴스가 상태를 공유할 수 있다

### 중복 방지 원리

- 모든 인스턴스가 같은 DB를 공유하고, 트리거 실행 직전 `QRTZ_LOCKS`에 행 락(`SELECT ... FOR UPDATE`)을 건다
- 락을 **먼저 잡은 한 노드만** 트리거를 `ACQUIRED`로 바꿔 실행 → 클러스터 전체에서 발사당 실행 1회
- `clusterCheckinInterval`로 노드 생존을 감지 → 죽은 노드가 물고 있던 잡을 산 노드가 인수(failover)
- `@DisallowConcurrentExecution` — 이전 실행이 안 끝났으면 다음 주기 skip (자기 겹침 방지)

### 설정 요점 (`application.yml`)

- `spring.quartz.job-store-type: jdbc` + `jdbc.initialize-schema: always` — 잡 상태를 DB에 저장, Quartz 테이블 자동 생성
- `org.quartz.jobStore.isClustered: true`, `scheduler.instanceId: AUTO`
- H2 파일 모드 + `AUTO_SERVER=TRUE` — 같은 머신의 여러 인스턴스가 한 DB 파일 공유
- **한계**: `AUTO_SERVER`는 같은 머신 한정. 실제 배포는 공유 네트워크 DB(Postgres 등)로 URL만 교체하면 됨. `isClustered=true` 설정은 동일

### 범위 밖 (확장 지점)

- 처리량이 스케줄러 스레드풀을 포화시키면 → 트리거는 큐에 넣고 별도 워커가 소비 (Redis/Kafka)
- 멀티테넌시 → 테넌트별 쿼터/rate limit, 감사 로그
- 관측성 → 실행 이력 테이블·대시보드

# read-write 분산

- `@Transactional(readOnly = true)`를 트리거로 읽기는 replica, 쓰기는 master로 보냄

| 방식 | 프로파일 | 라우팅 주체 | 추가 설정 |
|------|----------|-------------|-----------|
| A. Replication Driver | `replication` | MySQL JDBC 드라이버 | URL 한 줄 |
| B. AbstractRoutingDataSource | `routing` | 애플리케이션(Spring) | 빈 직접 조립 |

## 방식 A — Replication Driver

- `jdbc:mysql:replication://master,replica/db` URL만 지정 (첫 호스트=master)
- 드라이버가 로드밸런싱·페일오버 자동 처리
- 표준 Spring Boot + JPA면 설정 없이 동작 (Spring이 `setReadOnly` 자동 호출 → [readonly-marking.md](./readonly-marking.md))
- 라우팅이 드라이버 내부라 관찰·제어 어려움

## 방식 B — AbstractRoutingDataSource

- `determineCurrentLookupKey()`로 WRITE/READ 직접 결정 → 라우팅 기준 자유, 로그 관찰 용이
- ⚠️ **`LazyConnectionDataSourceProxy` 필수** (누락 시 항상 master)

## 검증 (권장)

- Testcontainers가 `mysql:8.4` 2개를 GTID 복제로 자동 구성 (Docker 데몬만 필요)
- 읽기 → replica(2), 쓰기 → master(1) 확인
- Rancher Desktop이면 `DOCKER_HOST`를 `build.gradle.kts`가 자동 감지

```bash
./gradlew test
```

## 수동 실행

```bash
docker compose up -d        # master :3306 / replica :3307
./gradlew bootRun --args='--spring.profiles.active=replication'   # 또는 routing
curl -XPOST 'localhost:8080/users?name=alice'   # write → master
curl localhost:8080/users/where                 # readOnly → replica
```

- bitnami pull이 막히면 official `mysql:8.4` 2개로 binlog/GTID 복제 직접 구성 (동작 동일)

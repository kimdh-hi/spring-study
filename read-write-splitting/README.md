# read-write 분산

read-write 분산 두 방식 비교.

| 방식 | 프로파일 | 라우팅 주체 | 라우팅 단위 |
|------|----------|-------------|-------------|
| A. Replication Driver | `replication` | MySQL JDBC 드라이버 | 커넥션의 read-only 플래그 |
| B. AbstractRoutingDataSource | `routing` | 애플리케이션(Spring) | 트랜잭션의 readOnly 속성 |

두 방식 모두 `@Transactional(readOnly = true)`가 라우팅의 트리거다.

## 방식 A — Replication Driver

- `jdbc:mysql:replication://master,replica/db` URL만 지정 (첫 호스트=master, 나머지=replica). Kotlin config 0줄
- 다중 replica 로드밸런싱·페일오버를 드라이버가 자동 처리
- 라우팅이 드라이버 내부라 제어·관찰이 어렵고, 기준은 read-only로 고정
- ⚠️ **JPA에선 `hibernate.connection.handling_mode=DELAYED_ACQUISITION_AND_HOLD`가 없으면 라우팅이 동작하지 않는다** (→ [readonly-marking.md](./readonly-marking.md))

## 방식 B — AbstractRoutingDataSource

- `determineCurrentLookupKey()`에서 WRITE/READ 키를 직접 결정 → 라우팅 기준 자유(테넌트·어노테이션 등), 로그로 관찰 용이
- DataSource·LazyProxy 빈을 직접 조립하고 로드밸런싱·페일오버도 자가 구현
- ⚠️ **`LazyConnectionDataSourceProxy` 필수** (누락 시 항상 master로 감)

## 실행

### 1. DB 기동 (master-replica 복제)
```bash
docker compose up -d
```
- master: `localhost:3306` / replica: `localhost:3307`

### 2. 방식 A
```bash
./gradlew bootRun --args='--spring.profiles.active=replication'

curl -XPOST 'localhost:8080/users?name=alice'   # write → master
curl localhost:8080/users/where                 # readOnly → replica serverId
```

### 3. 방식 B
```bash
./gradlew bootRun --args='--spring.profiles.active=routing'
```
로그에 라우팅 키 선택이 찍힌다:
```
Routing -> WRITE (master)   # POST /users
Routing -> READ (replica)   # GET /users/where
```

### 응답 예시
```json
{ "routedBy": "READ(readOnly tx)", "serverId": 200, "readOnly": true }
```

## bitnami 이미지 pull 실패 시
2025년 bitnami 정책 변경으로 `bitnami/mysql:8.4` pull이 막힐 수 있다.
이 경우 official `mysql:8.4` 두 인스턴스를 띄우고 init 스크립트로 binlog/GTID + 복제를 구성하면 된다(라우팅 코드는 동일하게 동작).

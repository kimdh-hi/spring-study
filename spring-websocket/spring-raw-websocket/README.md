# spring-raw-websocket

- STOMP 없이 raw WebSocket으로 다중 인스턴스 채팅을 구성한 학습 모듈
- 방 참여는 영속 데이터(`room_user`), 방 수신은 연결 수명의 구독으로 분리

## 구조

| 레이어 | 구성 요소 | 역할 |
|---|---|---|
| `ui/ws` | `SessionHandshakeInterceptor` | 티켓 검증 후 `userId` / `deviceId` / `sessionId` 주입 |
| `ui/ws` | `WebSocketMessageHandler` | 연결·프레임·pong·종료 콜백 처리, 중복 세션 종료 및 전파 |
| `ui/api` | `WebSocketTicketController` | 핸드셰이크용 1회성 티켓 발급 |
| `ui/api` | `RoomUserController` | 본인 방 가입/탈퇴/목록 (REST) |
| `ui/api` | `ChatMessageController` | 재연결 구간 메시지 백필 |
| `application/chat` | `ChatService` | 구독 검증 후 저장·팬아웃 |
| `application/chat` | `RoomUserService` | 멤버십 변경 + `RoomUserChangedEvent` 발행 |
| `infra/ws` | `SessionRegistry` | `sessionId` / `deviceId` / `roomId` 인덱스 |
| `infra/ws` | `RoomSubscriptions` | 구독 부착·해제, Redis 방 채널 개폐 |
| `infra/ws` | `ConnectionHeartbeat` | ping 발송 및 pong 타임아웃 세션 정리 |
| `infra/redis` | `RoomChannelRegistrar` | 방 채널 런타임 구독/해제 |
| `infra/redis` | `FanoutRedisSubscriber` | 중복 세션 종료·멤버십 변경 수신 |
| `infra/redis` | `RoomUserChangedRedisBridge` | 커밋 이후 멤버십 변경 전파 |

## 멤버십과 구독의 분리

- `room_user` 테이블이 멤버십의 source of truth이고 명시적 탈퇴까지 영속
- 메모리 인덱스는 "현재 접속 세션의 구독"이며 연결 수명만 유지
- 두 상태의 수명이 다르므로 동기화 경로가 필요

| 시점 | DB | 구독 인덱스 |
|---|---|---|
| WebSocket 연결 | `findRoomIdsByUserId` 1회 | 전체 등록 |
| REST 가입 | insert | 접속 중 세션에 추가 |
| REST 탈퇴 | delete | 접속 중 세션에서 제거 |
| WebSocket 종료 | 변경 없음 | 전체 해제 |

- 메시지 발송 시 DB 조회 없음 → 검증과 팬아웃 대상 모두 메모리 인덱스에서 결정
- 쿼리는 연결당 1회로, STOMP의 구독당 1회와 동일한 비용 구조

## 멤버십 변경 전파

- `RoomUserService`가 `RoomUserChangedEvent`를 발행
- `RoomUserChangedRedisBridge`가 `@TransactionalEventListener(AFTER_COMMIT)`로 받아 Redis 발행
- 커밋 이후에만 발행하므로 롤백 시 유령 구독이 남지 않음
- 각 노드는 해당 `userId`의 모든 세션에 구독을 반영 (다중 디바이스 포함)
- 서버가 구독 집합을 직접 소유하므로 클라이언트 협조 없이 즉시 반영
- 상태 변경을 알리는 송신 프레임은 없으며, 클라이언트는 `GET /api/me/rooms`로 갱신

## 팬아웃

- 채널을 방 단위로 분리: `ws:fanout:room:{roomId}`
- 노드는 로컬 구독자가 0 → 1이 될 때 채널을 열고, 1 → 0이 될 때 닫음
- 관심 없는 방의 메시지를 애초에 수신하지 않음
- 페이로드는 와이어 프레임 그대로이며 대상은 채널 이름이 결정

| 채널 | 용도 |
|---|---|
| `ws:fanout:room:{roomId}` | 방 메시지 |
| `ws:fanout:room-user` | 멤버십 변경 |
| `ws:fanout:duplicate-session` | 중복 세션 종료 |

## 세션 신뢰성

- 동일 `deviceId` 신규 연결 시 이전 세션을 `1008 duplicate session`으로 종료
- 인덱스를 `sessionId` 기준으로 유지하여 교체 중에도 방 구독이 끊기지 않음
- 30초마다 ping 발송, 90초 내 pong이 없으면 세션 종료
- Redis pub/sub은 at-most-once이므로 재연결 구간은 백필 API로 보완

## 프로토콜

### 신원

- REST는 `X-User-Id` / `X-Device-Id` 헤더로 호출자를 식별
- 모든 엔드포인트가 호출자 본인만 대상으로 하며, 타인을 가입/탈퇴시키는 경로가 없음
- `POST /api/ws-tickets` → `{ "ticket": "..." }`, TTL 60초 1회성
- `ws://{host}/ws/chat?ticket={ticket}` 연결 시 티켓을 소비하며 신원 확정
- 소켓은 쿼리 파라미터로 받은 `userId`를 신뢰하지 않고 티켓에서만 신원을 얻음

### 수신 프레임 (client → server)

| type | 필드 |
|---|---|
| `chat.send` | `roomId`, `text` |

### 송신 프레임 (server → client)

| type | 필드 |
|---|---|
| `chat.message` | `id`, `roomId`, `senderUserId`, `senderDeviceId`, `text`, `at` |

### REST

| 메서드 | 경로 | 용도 |
|---|---|---|
| `POST` | `/api/ws-tickets` | 핸드셰이크 티켓 발급 |
| `PUT` | `/api/rooms/{roomId}/me` | 본인 가입 |
| `DELETE` | `/api/rooms/{roomId}/me` | 본인 탈퇴 |
| `GET` | `/api/me/rooms` | 본인 방 목록 |
| `GET` | `/api/rooms/{roomId}/messages?afterId=&limit=` | 메시지 백필 |

- 전부 `X-User-Id` 헤더 필수, 티켓 발급은 `X-Device-Id`도 필요

## 실행

```bash
docker compose up -d
./gradlew bootRun
```

- 브라우저에서 `http://localhost:8080` 접속
- 연결 직후 서버가 방 목록을 내려주지 않으므로 `GET /api/me/rooms`로 조회
- 다중 인스턴스 확인은 `--args='--server.port=8081'`로 두 번째 인스턴스 기동

## 테스트

```bash
./gradlew test
```

- `SessionRegistryTest` — 구독 전이, 세션 교체, pong 타임아웃 단위 검증
- `ChatFlowIntegrationTest` — Testcontainers Redis 기반 실제 WebSocket 연결 시나리오
- Rancher Desktop 등 `/var/run/docker.sock`이 없는 환경은 `TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock` 필요

## STOMP와의 비교

| | raw (이 모듈) | STOMP + user destination | STOMP + `/topic` |
|---|---|---|---|
| 메시지당 DB 조회 | 0 | 1 | 0 |
| 구독 레지스트리 | 앱 | 앱 | 브로커 |
| 멀티 인스턴스 | Redis 팬아웃 직접 | Redis 팬아웃 직접 | 브로커 릴레이 |
| 권한 검증 | 연결당 1회 | 메시지당 | 구독당 1회 |
| 프레임 파싱·하트비트 | 직접 | 프로토콜 제공 | 프로토콜 제공 |

- raw와 STOMP의 실질 차이는 프로토콜 보일러플레이트에서 발생
- 라우팅 비용은 구독 모델을 도입하면 동등해짐
- 대신 브로커 릴레이가 주는 전역 구독 레지스트리는 raw로 재현할 수 없어 Redis 채널 개폐로 근사

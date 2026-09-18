# spring websocket

## spring websocket(raw websocket)

- https://docs.spring.io/spring-framework/reference/web/websocket/server.html

### WebSocketConfig (WebSocketConfigurer)

- interceptor(HandshakeInterceptor), handler(TextWebSocketHandler) 등록
- deviceHandshakeInterceptor, deviceWebSocketHandler

### WebSocketHttpRequestHandler.handleRequest websocket protocol upgrade

- ws 업그레이드 전, 후로 interceptor 순회 호출 (HandshakeInterceptor 포함)
  - ws 업그레이 전에는 정방향, 후에는 역방향 순회
  - 정방향 순회시 HandshakeInterceptor.beforeHandshake
  - 역방향 순회시 HandshakeInterceptor.afterHandshake
- interceptor 정방향 순회 이후 protocol upgrade 응답 구성 (doHandshake)
  - 101 switching protocols 응답
- doHandshake 이후 HandshakeInterceptor.afterHandshake 호출 역방향 순회
  - 단, beforeHandshake 가 성공한 (true 를 반환한) 것만 대상이 됨
- attributes 에 설정한 값 WebsocketHandler 에서 참조 가능 (WebSocketSession.getAttributes())
- 주로 WebSocketHandler 로 공개한 endpoint 로 websocket 연결 요청시 전달되는 파라미터들 기반으로 attributes 에 세팅하여 이후 handler 에서 사용하는 용도로 사용

### WebsocketHandler.afterConnectionEstablished

- 101 응답 이후, interceptor before/after 이후 첫 프레임 전송 되기 전 afterConnectionEstablished 호출됨
- interceptor 통해 websocket 연결이 수립된 완료된 이후이므로 websocket session 관리 등을 주로 담당

### ConcurrentWebSocketSessionDecorator

- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/socket/handler/ConcurrentWebSocketSessionDecorator.html
- ConcurrentWebSocketSessionDecorator 없이 n개 스레드가 같은 세션에 WebSocketSession.sendMessage() 시 뒤에 호출한 스레드는 대기하는 것이 아닌 예외 발생
  - 앞 sendMessage 가 아직 진행중인 경우 예외 발생
- ConcurrentWebSocketSessionDecorator 도입이 sendMessage() 호출은 버퍼에 적재
  - 버퍼에 적재한 후 락 획득 시도
  - 획득한 경우 버퍼가 빌 때까지 poll 하여 버퍼 내 모든 메세지 처리 (다른 스레드가 적재한 메세지까지 모두 포함)
  - 획득 실패한 경우 즉시 리턴 (해당 스레드가 적재한 메세지만 버퍼에 남음)

```java
// delegate: 원본 webSocketSession
// sendTimeLimit: ms 단위 (stomp 기준 default: 10s)
//   - A스레드에서 세션에 메세지 보내서 아직 처리중
//   - B스레드에서 세션에 메세지 발송 위해 버퍼에 메세지 적재 이후 현재 처리중인 메세지가 어느정도의 시간동안 처리중인지 체크
//   - sendTimeLimit 를 초과한 경우 B스레드는 예외 (SessionLimitExceededException)
//   - 메세지 발송이 완료되면 sendStartTime 타이머 0으로 리셋
// bufferSizeLimit: 버퍼에 쌓인 모든 메세지 payload 길이의 합 상한(bytes)
// overflowStrategy
//   - bufferSizeLimit 초과시 동작 결정
//   - TERMINATE(default): SessionLimitExceededException 예외던지고 세션 종료
//   - DROP: 버퍼 크기가 bufferSizeLimit 이하로 내려갈 때까지 오래된 메세지 poll 해서 폐기
public ConcurrentWebSocketSessionDecorator(
        WebSocketSession delegate, int sendTimeLimit, int bufferSizeLimit,
        OverflowStrategy overflowStrategy)
```

---

## spring stomp
- https://docs.spring.io/spring-framework/reference/web/websocket/stomp.html

### stomp (Simple Text Oriented Messaging Protocol)
- https://docs.spring.io/spring-framework/reference/web/websocket/stomp/overview.html
- https://stomp.github.io/stomp-specification-1.2.html
- stomp 는 중개 서버를 통해 클라이언트 간 비동기 메세지 전달을 위한 상호운용 가능한 프로토콜로 클라이언트와 서버가 주고받는 메세지의 텍스트 기반 포맷을 정의
  - TCP와 같이 신뢰성 있는 양방향 스트리밍 프로토콜 위에서 프레임을 주고 받는다.

#### 프레임 구성
- command + header(optional) + body(optional)

```
COMMAND
header1:value1
header2:value2

Body^@ // ^@: NULL 바이트(프레임의 끝)
``` 

- command 목록
  - client: CONNECT/STOMP, SEND, SUBSCRIBE/UNSUBSCRIBE, ACK/NACK, BEGIN/COMMIT, ABORT, DISCONNECT
  - server: CONNECTED, MESSAGE, RECEIPT, ERROR
- server 는 STOMP 프레임과 CONNET 프레임을 동일 처리

#### stomp 연결 수립
- websocket 서브 프로토콜로써 stomp 사용시 연결 수립 흐름

```
1. tcp 연결 수립

2. HTTP GET websocket Upgrade
GET /ws HTTP/1.1
Host: example.com
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==
Sec-WebSocket-Version: 13
Sec-WebSocket-Protocol: v12.stomp, v11.stomp, v10.stomp # subprotocol 로 stomp 버전 명시 (선호하는 순으로 나열)
Origin: https://example.com

3. 101 Switching protocol 응답
HTTP/1.1 101 Switching Protocols
Upgrade: websocket
Connection: Upgrade
Sec-WebSocket-Accept: s3pPLMBiTxaQ9kYGzzhZRbK+xOo=
Sec-WebSocket-Protocol: v12.stomp

4. client 측에서 CONNECT(or STOMP) 프레임 send
CONNECT
accept-version:1.0,1.1,1.2
host:example.com
heart-beat:10000,10000

^@

5. server 측에서 CONNECTED 프레임 응답 및 stomp 세션 성립
CONNECTED
version:1.2
heart-beat:10000,10000
session:session-Wm0J0tX1vQ9C8Z

^@
```

#### heartbeat
- tcp 연결이 살아있는지 확인하고, 중간 네트워크 장치에 의해 연결이 끊기지 않도록 주기적으로 트래픽을 보내기 위한 용도
- client/server 각각 독립적으로 서로에게 heartbeat 발송
- heartbeat 는 stomp 프레임 구조는 아니고 단순히 "\n" 만 보냄
- CONNECT/CONNECTED 프레임 헤더로 설정
- `heart-beat:<A>,<B>`
- <A>: 송신 주기
  - 0: heartbeat 보낼수 없음
  - 자신이 보장하는 최소 간격의 하트비트 ms 단위 주기
- <B>: 수신 희망 주기
  - 0: heartbeat 받고 싶지 않음
  - 하트비트를 받고자 하는 ms 단위 주기
- heart-beat 헤더는 optional 이므로 생략되는 경우 `heart-beat:0,0` 과 동일하게 처리되어야 함

```
CONNECT   heart-beat:<cx>,<cy>
CONNECTED heart-beat:<sx>,<sy>
```

- client 측 하트비트 주기 판단
  - MAX(<cx>, <sy>)
- server 측 하트비트 주기 판단
  - MAX(<sx>, <cy>)
- 단, MAX 판단 대상 cx/sy, sx/cy 중 하나라도 0이 있는 경우 heartbeat 보내지 않음
- 즉, server 에서만 heartbeat 를 보낸다고 해서 CONNECT 시 heartbeat 헤더 생략하는 경우 cx/cy 0,0으로 설정되어 서버측 heartbeat 도 보내지지 않음.
- 서버만 보내고 싶은 경우 아래처럼 설정
  - client: 나는 못보내. 너는 10초 주기로 보내
  - server: 너는 보내지마. 나는 10초 주기로 보낼께

```
CONNECT
accept-version:1.2
host:example.com
heart-beat:0,10000

^@

CONNECTED
version:1.2
heart-beat:10000,0

^@
```

#### destination
- stomp 서버는 메세지를 보낼 수 있는 destination의 집합이다.
- 문자열로 구성된 destination은 send, subscribe, message command의 필수 헤더이다.
- 서버는 destination 에 대한 정의를 갖고, client 에게 어떤 destination 들이 있는지 알려주면 client 는 구독 필요한 destination을 구독한다.
- SEND command 통해 destination 에 stomp 프레임 발송시 해당 destination을 구독하는 client에게 stomp 프레임을 전달한다.
  - 구독자에게 stomp 프레임 전달시에는 MESSAGE command 가 사용됨 (client측 발송: SEND, server측 발송: MESSAGE)
  - 모든 구독자에게 전달할지(topic) 한 명의 구독자가 소비하도록 할지는 서버측 구현으로 스펙에서 정의하지 않는다.

#### subscribe/unsubscribe

```
SUBSCRIBE
id:sub-0 // client가 임의로 정한 값 (required)
destination:/topic/news

^@


UNSUBSCRIBE
id:sub-0 // 구독시 client가 임의로 정한 값 (required)

^@
```

- 스펙은 동일 client 가 동일 destination 을 중복으로 구독하는 것을 허용
  - 동일 destination 중복 구독시 해당 destination 의 1회 SEND시 2개 MESSAGE 프레임 수신
- 동일 destination 중복 구독시 특정 구독만 해제하는 처리가 필요하므로 별도 id 를 부여하고 id 를 통해 구독 해제

#### graceful DISCONNECT (receipt 헤더, RECEPIT 프레임)
- https://stomp.github.io/stomp-specification-1.2.html#Connection_Lingering
- client 는 언제든 tcp 연결을 닫을 수 있음
- 단, SEND 직후 socket.close() 시 client는 보냈다고 생각하지만 아직 송신 버퍼에 남아있을 수 있음
- socket.close() 이전 DISCONNECT 프레임 발행시 receipt 헤더를 설정
- 서버는 RECEIPT 프레임에 이전 receipt 헤더와 동일한 값을 설정하여 응답
- client 는 RECEIPT 프레임 수신시 이전 발행한 프레임들이 정상적으로 서버에 도달했음을 인지하고 연결 종료
- recepit 헤더와 RECEPIT 프레임은 graceful DISCONNECT 뿐만이 아니라 SEND 프레임에 대한 도돨 여부 확인에도 사용 가능



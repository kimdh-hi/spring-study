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
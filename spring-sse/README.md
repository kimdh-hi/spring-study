# spring sse

- https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-ann-async.html#mvc-ann-async-sse

## SseEmitter

- 컨트롤러가 `SseEmitter` 반환시 `text/event-stream` 비동기 응답 시작
- 요청 스레드는 반환 즉시 Tomcat 에 반납, HTTP 커넥션은 계속 유지
- 이후 다른 스레드에서 `emitter.send()` 로 이벤트 전송
- 이벤트 필드는 `id`, `event`(name), `data`, `retry`(reconnectTime), `:`(comment)

## 연결 수명

- 생성자 timeout 경과시 `onTimeout` -> `onCompletion` 호출
- timeout 미지정시 `spring.mvc.async.request-timeout`(서버 기본값) 적용
- 클라이언트 종료는 서버가 바로 알 수 없고 send 실패 시점에 `onError` 로 감지
- `onCompletion` 에서 registry 제거해야 메모리 누수 방지

## heartbeat

- 끊긴 연결 감지와 프록시 idle timeout 방지 목적
- comment(`:ping`) 이벤트는 브라우저 EventSource 리스너에 전달되지 않음

## 재연결 (Last-Event-ID)

- EventSource 는 끊기면 `retry` 시간 후 자동 재연결
- 재연결 요청에 마지막 수신 `id` 를 `Last-Event-ID` 헤더로 전송
- 유실 이벤트 복구는 서버가 이벤트를 저장해두고 재전송해야 가능 (샘플 생략)

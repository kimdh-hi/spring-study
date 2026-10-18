# spring sse

## sse?

- 끝나지 않는 HTTP 응답 1개로 서버 -> 클라이언트 단방향 U전송
- WHATWG HTML 표준 정의, HTTP 자체에는 SSE 규정 없음
- 쿠키, 인증, 프록시, HTTP/2 등 기존 HTTP 인프라 그대로 사용

```
HTTP/1.1 200 OK
Content-Type: text/event-stream

id: 1
event: message
data: hello

:ping
```

- `field: value` 줄 + 빈 줄로 이벤트 1건 구분
- field 종류: `data`, `event`, `id`, `retry` 4종

| | 요청 | 응답당 데이터 | 프로토콜 |
|---|---|---|---|
| 폴링 | 주기마다 | 1건 | HTTP |
| 롱폴링 | 이벤트마다 | 1건 | HTTP |
| SSE | 1회 | 무한 | HTTP |
| WebSocket | 1회 (Upgrade) | - | WebSocket 프레임 (양방향) |

## 연결 유지 원리

- TCP 는 FIN/RST 전까지 유지, 무송신 상태도 정상
- HTTP 응답 종료 조건은 `Content-Length` 충족, 마지막 청크(`0\r\n\r\n`), 연결 close
- SSE 서버는 셋 다 하지 않아 클라이언트가 계속 대기
- 실제 끊김 원인은 서버 timeout, 프록시/LB idle timeout, 클라이언트 종료 등에 의함

## SseEmitter

- 반환시 `startAsync()` 로 요청 스레드 반납, 소켓만 유지
- 다른 스레드에서 `send()` 로 청크 write + flush
- `complete()` 또는 timeout 시 마지막 청크 전송 후 종료
- 클라이언트 종료는 send 실패 시점에 `onError` 로 감지

## heartbeat

- 끊긴 연결 감지와 프록시 idle timeout 방지 목적
- comment(`:ping`) 는 `EventSource` 리스너에 미전달

## 재연결 (Last-Event-ID)

- `EventSource` 는 끊기면 `retry` 시간 후 자동 재연결
- 재연결 요청에 마지막 `id` 를 `Last-Event-ID` 헤더로 전송
- 유실 이벤트 복구는 서버 측 이벤트 저장 필요 (샘플 생략)

## 참고

- [WHATWG - Server-sent events](https://html.spec.whatwg.org/multipage/server-sent-events.html)
- [Spring SSE](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-ann-async.html#mvc-ann-async-sse)
- [MDN - Using server-sent events](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events)


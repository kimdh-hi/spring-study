## server.shutdown=graceful

- A서버 -> B서버 요청이 빈번한 경우 B서버 종료시 A서버는 응답을 받지 못하고 타임아웃이 발생할 가능성이 있음

```
B서버의 종료 설정이 `IMMEDIATE` 로 설정됐기 때문임


org.springframework.boot.autoconfigure.web.ServerProperties

// 기본설정이 IMMEDIATE 임
private Shutdown shutdown = Shutdown.IMMEDIATE;
```

`GRACEFUL` 로 종료 설정을 설정한 경우
- B서버 종료시 새로운 요청은 받지 않고 기존 요청에 대해서만 응답까지 처리 후 서버를 종료한다.
- 즉, 클라이언트에 해당하는 서버는 갑작스러운 요청 서버 종료에 의해 타임아웃에 걸리는 것을 방지할 수 있다.

`GRACEFUL` 설정에 의해 처리되던 작업에서 데드락 등 처리를 완료할 수 없는 케이스
- 서버 종료가 되지 않는 문제가 생김
```
# default: 30s
spring:
  lifecycle:
    timeout-per-shutdown-phase:10s
```
- 10초 후 `GRACEFUL` 로 설정된 상태라도 서버를 강제 종료한다.

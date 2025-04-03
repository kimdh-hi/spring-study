## graceful shutdown

### graceful shutdown?
- springboot application 종료시 진행중인 요청을 안전하게 종료하는 기능
- springboot 2.3 부터 지원
- tomcat, jetty, netty 등 지원
- springboot 3.4.0 graceful shutdown default 로 지정
  - https://github.com/spring-projects/spring-boot/commit/814369e8b05621439a963f37d2c2b50f1bb3c329
  - `server.shutdown: graceful`

### 설정
```yml
server.shutdown=graceful|immediate #default: graceful (immediate: graceful disable)

spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s # default: 30s
```
- `spring.lifecycle.timeout-per-shutdown-phase`: graceful 종료 최대 대기 시간, 초과시 강제 종료 (SIGKILL)

```
IDE 에서 실행 종료시 graceful shutdown 동작 안 될 수 있음.
```


### SIGTERM(SIGINT) / SIGKILL

```
> kill -l

HUP INT QUIT ILL TRAP ABRT EMT FPE KILL BUS SEGV SYS PIPE ALRM TERM URG STOP TSTP CONT CHLD TTIN TTOU IO XCPU XFSZ VTALRM PROF WINCH INFO USR1 USR2
```


SIGTERM(SIGINT)
- SIGTERM (signal + terminate) kill -15 (default)
- SIGINT (signal + interrupt) kill -2 (ctrl + c)
- SIGTERM, SIGINT 모두 해당 signal 에 대한 핸들링 가능 
  - if SIGTERM ...... else if SIGINT .... ?
- signal 에 대한 핸들링이 가능하므로 graceful shutdown 처리 가능


SIGKILL
- kill -9
- signal 에 대한 핸들링 불가. (= graceful shutdown 불가)

---

### referecne
- https://docs.spring.io/spring-boot/reference/web/graceful-shutdown.html
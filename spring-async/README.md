## spring async

### default TaskExecutor
- ThreadPoolTaskExecutor
    - TaskExecutionAutoConfiguration 참고
- springboot autoconfiguration 미사용 시 SimpleAsyncTaskExecutor (pool x) 사용

---

### TaskExecutor customizing
```yaml
spring:
  task:
    execution:
      pool:
        core-size: 8 # default: 8
        max-size: 16 # default: Integer.MAX_VALUE
        queue-capacity: 100 # default: Integer.MAX_VALUE
```
- core-size: pool 최소 스레드 수
- max-size: pool 최대 스레드 수
- queue-capacity: 비동기 작업 대기열 크기

thread name custom
- default: task-1
    -  TaskExecutionProperties (default 설정 관련 코드 확인)

```yaml
spring:
  task:
    execution:
      thread-name-prefix: "async-"
```

---

### async method ThreadLocal 사용
- @Async 사용시 thread 가 달라지므로 기존 thread 에서 사용중인 ThreadLocal 사용 불가.
- 기존 thread 의 ThreadLocal 사용하려면 ThreadLocal 값 복사 필요.
- ThreadPoolTaskExecutor에 TaskDecorator 설정시 비동기 처리 taskExecutor 커스터마이징 가능
- AsyncConfig.threadPoolTaskExecutorCustomizer 참조
- test: UserServiceTest.test userIdHolder log 확인

```
//SecurityContextHolder 사용하는 경우
@Bean
fun threadPoolTaskExecutorCustomizer() = ThreadPoolTaskExecutorCustomizer {
  it.setTaskDecorator { runnable -> DelegatingSecurityContextRunnable(runnable) }
}
```
- 두 개 이상 taskDecorator 지정시 마지막 taskDecorator 만 지정됨 

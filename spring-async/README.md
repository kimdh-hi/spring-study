## spring async

### default TaskExecutor
- ThreadPoolTaskExecutor
    - TaskExecutionAutoConfiguration 참고
- springboot autoconfiguration 미사용 시 SimpleAsyncTaskExecutor (pool x) 사용

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

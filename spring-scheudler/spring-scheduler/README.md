## Spring Scheduler

```kotlin
  @Scheduled(fixedDelay = 1000)
  fun scheduler1() {
    log.info("scheduler1...")
    Thread.sleep(10_000)
  }

  @Scheduled(fixedDelay = 1000)
  fun scheduler2() {
    log.info("scheduler2...")
  }
```

`scheduler1` thread sleep 후 `scheduler2` 가 별도 thread 에서 실행되기를 기대
- `scheduler1` thread sleep 10 초 후 `scheduler2` 실행됨

scheduler thread pool 설정

- code 로 설정
```kotlin
@Configuration
@EnableScheduling
class SchedulerConfig: SchedulingConfigurer {

  override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
    val threadPool = ThreadPoolTaskScheduler().apply {
      poolSize = Runtime.getRuntime().availableProcessors()
      initialize()
    }

    taskRegistrar.setTaskScheduler(threadPool)
  }
}
```
- 프로퍼티 설정
```yaml
spring.task.scheduling.pool.size: 5
```
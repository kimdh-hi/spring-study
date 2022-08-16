
## Jackson2JsonMessageConverter
mq 로 메시지 전송시 매 번 json으로 변환해주어야 하는 번거로움이 있다.

```kotlin
val json = objectMapper.writeValueAsString(message)
```

Jackson2JsonMessageConverter 를 사용하면 객체타입을 그대로 mq에 보낼 수 있다.
Consumer 측에서는 `__TypeId__` 를 기준으로 역직렬화 대상 객체를 찾는다.

```
__TypeId__ 는 Producer 에 의해 mq 에 보내지는 객체의 풀패키지 경로이다.
```


## Consumer on / off (RabbitListenerEndpointRegistry)

특정 시간 동안 모든 Consumer를 stop 혹은 start 한다.

```kotlin
@Service
@EnableScheduling
class RabbitmqScheduler(
  private val registry: RabbitListenerEndpointRegistry
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Scheduled(cron = "0 55 21 * * *")
  fun stopAll() {
    registry.listenerContainers.forEach {
      log.info("stop rabbitmq container: {}", it)
      it.stop()
    }
  }

  @Scheduled(cron = "0 57 21 * * *")
  fun startAll() {
    registry.listenerContainers.forEach {
      log.info("start rabbitmq container: {}", it)
      it.start()
    }
  }
}

```
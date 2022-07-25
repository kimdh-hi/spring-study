package `04-cold_hot-publisher`

import `00-base`.ConsumerUtils
import reactor.core.publisher.Flux
import java.time.Duration

fun main() {
  val flux = Flux.range(1, 1000)
    .delayElements(Duration.ofSeconds(1))

  flux
    .log()
    .subscribe(ConsumerUtils.subscriber("sub1"))

  Thread.sleep(5000L)

  // 5초 후에 새로운 publisher 로부터  subscribe 를 시작 - cold publisher ...
  flux
    .log()
    .subscribe(ConsumerUtils.subscriber("sub2"))

  Thread.sleep(1000000L)

}
package `04-cold_hot-publisher`

import `00-base`.ConsumerUtils
import reactor.core.publisher.Flux
import java.time.Duration

fun main() {
  val flux = Flux.range(1, 1000)
    .delayElements(Duration.ofSeconds(1))
    .share() // hot-shared publisher

  flux
    .log()
    .subscribe(ConsumerUtils.subscriber("sub1"))

  Thread.sleep(5000L)

  // 처음부터가 아닌 consume 되던 지점부터 consume 시작
  flux
    .log()
    .subscribe(ConsumerUtils.subscriber("sub2"))

  Thread.sleep(1000000L)

}
package `04-cold_hot-publisher`

import `00-base`.ConsumerUtils
import reactor.core.publisher.Flux
import java.time.Duration

fun main() {
  // emit 한 item 을 캐싱
  // 중간에 다른 subscriber 가 들어왔을 때 캐싱된 결과를 일괄 emit
  // cache(n) : 마지막 n개 item 을 캐싱 (default: Integer.MAX_VALUE)
  val flux = Flux.range(1, 1000)
    .delayElements(Duration.ofSeconds(1))
    .cache(3)

  flux
    .log()
    .subscribe(ConsumerUtils.subscriber("sub1"))

  Thread.sleep(10000L)

  flux
    .log()
    .subscribe(ConsumerUtils.subscriber("sub2"))

  Thread.sleep(1000000L)

}
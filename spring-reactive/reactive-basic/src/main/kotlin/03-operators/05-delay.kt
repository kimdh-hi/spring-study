package `03-operators`

import `00-base`.ConsumerUtils
import reactor.core.publisher.Flux
import java.time.Duration

fun main() {
  Flux.range(1, 100)
    .log()
    .delayElements(Duration.ofSeconds(1))
    .subscribe(ConsumerUtils.subscriber())

  Thread.sleep(100000L)
}
package `03-operators`

import `00-base`.ConsumerUtils
import reactor.core.publisher.Flux

fun main() {
  Flux.range(1, 1000)
    .log()
    .limitRate(100) // 75%
    .subscribe(ConsumerUtils.subscriber())
}
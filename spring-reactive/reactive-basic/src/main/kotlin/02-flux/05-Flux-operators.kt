package `02-flux`

import `00-base`.ConsumerUtils
import reactor.core.publisher.Flux

fun main() {
  Flux.range(1, 10)
    .log()
    .take(3) // n개 consume 후 complete
    .log() // onNext, onError, onComplete 등 확인가능
    .subscribe(ConsumerUtils.subscriber())
}
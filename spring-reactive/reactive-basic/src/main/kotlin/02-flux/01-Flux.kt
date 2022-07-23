package `02-flux`

import `00-base`.ConsumerUtils
import reactor.core.publisher.Flux

fun main() {
  Flux.create {
    it.next(1)
    it.next(2)
    it.complete()
  }.subscribe(ConsumerUtils.subscriber())
}
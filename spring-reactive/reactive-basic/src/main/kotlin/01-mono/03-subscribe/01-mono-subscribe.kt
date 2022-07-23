package `01-mono`.`03-subscribe`

import `00-base`.ConsumerUtils
import reactor.core.publisher.Mono

fun main() {
  val mono1 = Mono.just("hello")
    .map { it.length }
  mono1.subscribe(
    { item -> println("consume: $item") },
    { e -> println(e.message) },
    { println("onComplete...") }
  )

  val mono2 = Mono.just("hello")
    .map { it.length }
    .map { it / 0 }
  mono2.subscribe(
    ConsumerUtils.onNext(),
    ConsumerUtils.onError(),
    ConsumerUtils.onComplete()
  )
}
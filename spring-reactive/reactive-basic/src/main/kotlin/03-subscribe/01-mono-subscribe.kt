package `03-subscribe`

import reactor.core.publisher.Mono

fun main() {
  val mono = Mono.just("hello")
    .map { it.length }
    .map { it / 0 } // exception ...

  mono.subscribe(
    { item -> println("consume: $item") },
    { e -> println(e.message) },
    { println("onComplete...") }
  )
}
package `02-mono-just`

import reactor.core.publisher.Mono

fun main() {
  // publisher
  val mono = Mono.just(1)

  println(mono) // nothing happen ... before subscribe

  // subscriber
  mono.subscribe { println("sub: $it") }
}
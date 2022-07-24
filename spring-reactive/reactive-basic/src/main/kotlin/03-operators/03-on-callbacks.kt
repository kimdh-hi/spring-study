package `03-operators`

import `00-base`.ConsumerUtils
import reactor.core.publisher.Flux

fun main() {
  Flux.create{
    for (i in 1..10) {
//      throw RuntimeException("error...")
      it.next(i)
    }
    it.complete()
  }
    .doFirst { println("doFirst") }
    .doOnNext { println("doOnNext") }
    .doOnComplete { println("doOnComplete") }
    .doOnError { e -> println("doOnError e: ${e.message}") }
    .doFinally { signal -> println("doFinally signal: $signal") }
    .subscribe(ConsumerUtils.subscriber())

}
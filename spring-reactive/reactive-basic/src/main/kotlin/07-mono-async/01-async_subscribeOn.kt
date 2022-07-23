package `07-mono-async`

import `00-base`.ConsumerUtils
import `00-base`.FakerUtils
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

fun main() {
  getName()
  getName()
    .subscribeOn(Schedulers.boundedElastic()) // to async...
    .subscribe(ConsumerUtils.onNext()) // block ...
  getName()

  Thread.sleep(3000)
}

fun getName(): Mono<String> {
  println("start building getName pipeline ...")
  return Mono.fromSupplier {
    println("generating name ...")
    Thread.sleep(2000L)
    FakerUtils.getFullName()
  }.map { it.uppercase() }
}
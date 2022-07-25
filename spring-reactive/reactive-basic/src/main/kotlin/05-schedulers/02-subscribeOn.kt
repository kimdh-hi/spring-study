package `05-schedulers`

import `00-base`.PrintUtils
import `00-base`.SleepUtils
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

/**
 * subscribeOn
 * - subscribeOn 기준 upstream 의 일련의 작업은 별도의 worker thread 에서 수행된다.
 *
 * boundedElastic
 * - 블로킹 IO 등에 적합
 * - 작업을 위한 별도 worker thread 의 수가 제한된 것이 특징 (default: CPU core * 10)
 */

fun main() {

  val flux = Flux.create {
    println("create")
    it.next(1)
  }
    .doOnNext { PrintUtils.printThreadName("doOnNext...$it") }

//  flux
//    .doFirst { PrintUtils.printThreadName("first2") } // subscribeOn 기준 upstream ... ⬆️
//    .subscribeOn(Schedulers.boundedElastic())
//    .doFirst { PrintUtils.printThreadName("first1") } // subscribeOn 기준 downstream ... ⬇️
//    .subscribe { PrintUtils.printThreadName("subscribe...$it") }


  val runnable = Runnable {
    flux
      .doFirst { PrintUtils.printThreadName("first2") }
      .subscribeOn(Schedulers.boundedElastic())
      .doFirst { PrintUtils.printThreadName("first1") }
      .subscribe { PrintUtils.printThreadName("subscribe...$it") }
  }

  for (i in 1..2) {
    Thread(runnable).start()
  }

  SleepUtils.sleepForSeconds(5)

}

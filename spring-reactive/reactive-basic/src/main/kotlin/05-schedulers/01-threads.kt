package `05-schedulers`

import `00-base`.ConsumerUtils
import `00-base`.PrintUtils
import reactor.core.publisher.Flux

/**
 * 기본적으로 stream을 생성하고, emit(next) 하고, subscribe 하는 것은 한 개 쓰레드 (main Thread) 에서 동작한다.
 */

fun main() {

  val flux = Flux.create {
    println("create")
    it.next(1)
  }
    .doOnNext { PrintUtils.printThreadName("doOnNext...$it") }

  val runnable = Runnable { flux.subscribe { PrintUtils.printThreadName("subscribe...$it") }  }

  for (i in 1..2) {
    Thread(runnable).start()
  }

}

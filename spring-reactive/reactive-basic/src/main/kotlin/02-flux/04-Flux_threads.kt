package `02-flux`

import `00-base`.ConsumerUtils
import `02-flux`.helper.NameProducer
import reactor.core.publisher.Flux

fun main() {
  val nameProducer = NameProducer()
  Flux.create(nameProducer)
    .subscribe(ConsumerUtils.subscriber())

  val runnable = Runnable { nameProducer.produce() }
  for(i in 1..10) {
   Thread(runnable).start()
  }

  Thread.sleep(2000L)
}
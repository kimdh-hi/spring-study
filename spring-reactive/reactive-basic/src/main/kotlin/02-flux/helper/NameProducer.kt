package `02-flux`.helper

import `00-base`.FakerUtils
import reactor.core.publisher.FluxSink
import java.util.function.Consumer

class NameProducer: Consumer<FluxSink<String>> {

  private lateinit var fluxSink: FluxSink<String>

  override fun accept(fluxSink: FluxSink<String>) {
    this.fluxSink = fluxSink
  }

  fun produce() {
    val name = FakerUtils.FAKER.country().name()
    val threadName = Thread.currentThread().name
//    Thread.sleep(1000L)
    fluxSink.next("$threadName - $name")
//    fluxSink.complete()
  }
}
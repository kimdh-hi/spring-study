package `02-flux`

import `00-base`.ConsumerUtils
import `00-base`.FakerUtils
import reactor.core.publisher.Flux

fun main() {
  Flux.create {
    var country = ""
    while (!country.contains("Korea")) {
      country = FakerUtils.FAKER.country().name()
      it.next(country)
      if(country.contains("korea"))
        it.complete()
    }
  }.subscribe(ConsumerUtils.subscriber())
}
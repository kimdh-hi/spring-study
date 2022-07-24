package `03-operators`

import `00-base`.ConsumerUtils
import `00-base`.FakerUtils
import reactor.core.publisher.Flux

fun main() {
  Flux.generate { it.next(FakerUtils.FAKER.country().name()) }
    .map { it.toString() }
    .handle { countryName, sink ->
      sink.next(countryName)
      if (countryName.lowercase().contains("korea"))
        sink.complete()
    }.subscribe(ConsumerUtils.subscriber())
}
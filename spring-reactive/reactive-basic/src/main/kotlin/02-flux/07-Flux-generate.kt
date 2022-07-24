package `02-flux`

import `00-base`.ConsumerUtils
import `00-base`.FakerUtils
import reactor.core.publisher.Flux

fun main() {
  // 데이터를 무한하게 emit 한다.
  Flux.generate {
    val name = FakerUtils.FAKER.country().name()
    it.next(name)
    if (name.lowercase().contains("korea"))
      it.complete()
    // it.next(FakerUtils.FAKER.country().name()) // generate 는 한 개 flux instance? 만 사용되야 한다.
  }.subscribe(ConsumerUtils.subscriber())
}
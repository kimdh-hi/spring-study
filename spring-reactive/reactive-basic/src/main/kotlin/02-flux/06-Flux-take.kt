package `02-flux`

import `00-base`.ConsumerUtils
import `00-base`.FakerUtils
import reactor.core.publisher.Flux

fun main() {
  Flux.create {
    var country = ""
    while (!country.contains("Korea") && !it.isCancelled) {
      country = FakerUtils.FAKER.country().name()
      println("pushing: $country")
      it.next(country)
      println("emitting: $country")
      if(country.contains("korea"))
        it.complete()
    }
  }
    // 3번째 country 가 emit 될 때 onComplete 가 호출되지만
    // "korea" 에 걸릴 때까지 pushing 을 지속한다.
    // 데이터를 push 할 때 취소여부(isCanceled) 를 확인하자.
    .take(3)
    .subscribe(ConsumerUtils.subscriber())
}
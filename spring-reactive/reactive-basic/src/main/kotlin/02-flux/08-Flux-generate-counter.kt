package `02-flux`

import `00-base`.ConsumerUtils
import `00-base`.FakerUtils
import reactor.core.publisher.Flux

// korea 를 포함하는 국가가 5개 나올 때까지 emit
// counter 필요
fun main() {
  Flux.generate(
    { 1 }, // counter 초기값
    { counter, sink ->
      val name = FakerUtils.FAKER.country().name()
      sink.next(name)
      if (name.lowercase().contains("korea")) {
        if(counter < 5) return@generate counter+1 // counter 증가
        sink.complete()
      }
      counter
    }
  )
    .subscribe(ConsumerUtils.subscriber())
}
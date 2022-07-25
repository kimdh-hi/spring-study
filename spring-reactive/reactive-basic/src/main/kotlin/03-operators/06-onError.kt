package `03-operators`

import `00-base`.ConsumerUtils
import `00-base`.FakerUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Supplier

fun main() {
  Flux.range(1, 5)
    .log()
    .map { 10 / (4-it) }
//    .onErrorReturn(-1) // onError 시 default value emit 후 onComplete 호출
//    .onErrorResume { e -> Mono.fromSupplier { FakerUtils.FAKER.random().nextInt(1, 100) } } // onError 시 새로운 publisher 동작
    .onErrorContinue { t, u -> }
    .subscribe(ConsumerUtils.subscriber())
}

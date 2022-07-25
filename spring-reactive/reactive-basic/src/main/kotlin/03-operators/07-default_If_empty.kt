package `03-operators`

import `00-base`.ConsumerUtils
import reactor.core.publisher.Flux

fun main() {

  Flux.range(1, 10)
    .filter { it > 10 }
    .defaultIfEmpty(-1) // consume 된 것이 한 개도 없을 때의 기본값
    .subscribe(ConsumerUtils.subscriber())
}
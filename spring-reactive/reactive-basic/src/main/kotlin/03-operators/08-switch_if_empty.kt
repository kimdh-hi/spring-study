package `03-operators`

import `00-base`.ConsumerUtils
import reactor.core.publisher.Flux

fun main() {
  Flux.range(1, 10)
    .filter { it > 10 }
    .switchIfEmpty(defaultPublisher()) // consume 된 것이 하나도 없다면 다른 Publisher (stream) 을 구성가능
    .subscribe(ConsumerUtils.subscriber())
}

fun defaultPublisher() = Flux.range(100, 10)
package `03-operators`

import `00-base`.ConsumerUtils
import reactor.core.publisher.Flux

fun main() {

  // handle = map + filter
  Flux.range(1, 10)
    .handle { item, sink ->
      if (item % 2 == 0)
        sink.next(item)
    }.subscribe(ConsumerUtils.subscriber())

}
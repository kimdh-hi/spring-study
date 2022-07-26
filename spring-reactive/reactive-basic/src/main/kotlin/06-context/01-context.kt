package `06-context`

import `00-base`.ConsumerUtils
import `00-base`.SleepUtils
import reactor.core.publisher.Mono

fun main() {
  getWelcomeMessage()
    .contextWrite { it.put("user", "kim") }
    .subscribe(ConsumerUtils.subscriber())

}

fun getWelcomeMessage(): Mono<String> = Mono.deferContextual {
  if (it.hasKey("user"))
    Mono.just("Welcome!, ${it.get<String>("user")}")
  else
    Mono.error(RuntimeException("user not found..."))
}
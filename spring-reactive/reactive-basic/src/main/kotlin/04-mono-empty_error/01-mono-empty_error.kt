package `04-mono-empty_error`

import `00-base`.ConsumerUtils
import `00-base`.FakerUtils
import reactor.core.publisher.Mono

fun main() {
  val userRepository = UserRepository()
  userRepository.findById(3L)
    .subscribe(
      ConsumerUtils.onNext(),
      ConsumerUtils.onError(),
      ConsumerUtils.onComplete()
    )
}

class UserRepository {
  fun findById(id: Long): Mono<String> {
    return when (id) {
      1L -> Mono.just(FakerUtils.FAKER.name().firstName()) // call onNext and onComplete...
      2L -> Mono.empty() // null (only call onComplete)...
      else -> Mono.error(RuntimeException("user not found ...")) // call onError...
    }
  }
}
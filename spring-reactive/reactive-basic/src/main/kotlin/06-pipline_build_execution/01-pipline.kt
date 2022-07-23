package `06-pipline_build_execution`

import `00-base`.FakerUtils
import reactor.core.publisher.Mono

/**
 * getName 은 2초 동안 sleep 하는 연산을 갖고 있지만 main 함수는 getName 을 3번 호출하고 금방 종료한다.
 * getName 을 호출하는 것은 pipline 을 만들어내는 것이지 실제 실행되는 것이 아니다.
 * 실행은 반드시 subscribe 가 이뤄져야 한다.
 *
 * 시퀀스 생성을 위한 pipeline build 작업과 생성된 시퀀스를 execution 하는 것이 다르다는 것을 알자.
 */
fun main() {
  getName()
  getName()
  getName()
}

fun getName(): Mono<String> {
  println("start building getName pipeline ...")
  return Mono.fromSupplier {
    println("generating name ...")
    Thread.sleep(2000L)
    FakerUtils.getFullName()
  }.map { it.uppercase() }
}
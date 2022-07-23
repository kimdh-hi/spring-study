package `01-mono`.`05-mono-funtional_interface`

import `00-base`.ConsumerUtils
import `00-base`.FakerUtils
import reactor.core.publisher.Mono

fun main() {
  val byJustMono = Mono.just(getName())

  val bySupplierMono = Mono.fromSupplier { getName() }
  bySupplierMono.subscribe(
      ConsumerUtils.onNext()
    )

  val byCallableMono = Mono.fromCallable { getName("testName") }
  byCallableMono.subscribe(
    ConsumerUtils.onNext()
  )
}

// just 의 경우 즉시 시퀀스를 생성하기 때문에 생성하는 순간 println 이 실행된다.
// from 의 경우 subscribe 전까지 시퀀스 생성을 미룬다 (lazy)
// Mono 로 생성할 대상 전에 일련의 연산을 필요로 한다면 fromSupplier 를 사용하자.
// 오직 데이터만 Mono 시퀀스로 생성한다면 just 로 충분하다.
fun getName(): String {
  println("generating ...")
  return FakerUtils.FAKER.name().fullName()
}

fun getName(name: String): String {
  println("processing ...")
  return name
}
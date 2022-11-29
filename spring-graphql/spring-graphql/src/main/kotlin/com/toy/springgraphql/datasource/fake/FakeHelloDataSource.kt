package com.toy.springgraphql.datasource.fake

import com.github.javafaker.Faker
import com.toy.springgraphqldemo.generated.types.Hello
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class FakeHelloDataSource(
  private val faker: Faker
) {

  companion object {
    val HELLO_LIST = mutableListOf<Hello>()
  }

  @PostConstruct
  fun postConstructor() {
    for (i in 0 until 20) {
      val hello = Hello(
        text = faker.company().name(),
        randomNumber = faker.random().nextInt(5000)
      )
      HELLO_LIST.add(hello)
    }
  }
}
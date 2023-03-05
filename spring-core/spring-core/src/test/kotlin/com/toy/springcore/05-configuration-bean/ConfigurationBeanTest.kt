package com.toy.springcore.`05-configuration-bean`

import com.toy.springcore.config.FirstBean
import com.toy.springcore.config.SecondBean
import com.toy.springcore.config.SomeBean
import com.toy.springcore.config.ThirdBean
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ConfigurationBeanTest(
  private val someBean: SomeBean,
  private val firstBean: FirstBean,
  private val secondBean: SecondBean,
  private val thirdBean: ThirdBean,
) {
  @Test
  fun test() {
    println(someBean.printSomeBean())
    println(firstBean.printSomeBean())
    println(secondBean.print())
    println(thirdBean.print())
  }
}
package com.study.springcoreadvanced.`template-method`

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.util.StopWatch

/*
Template method
변하는 부분(비즈니스 로직) 을 변하지 않는(부가기능) 과 분리
 */
class TemplateMethodEx {
  private val log = LoggerFactory.getLogger(javaClass)

  @Test
  fun `TemplateMethod 적용 x`() {
    // 부가기능 ...
    val stopWatch = StopWatch()
    stopWatch.start()

    // 비즈니스 로직...
    Thread.sleep(1000L)
    log.info("business logic ...")

    // 부가기능 ...
    stopWatch.stop()
    log.info("execution time: {}", stopWatch.totalTimeMillis)
  }

  @Test
  fun `TemplateMethodV1 적용`() {
    val subClass1 = SubClass1()
    subClass1.execute()

    val subClass2 = SubClass2()
    subClass2.execute()
  }

  // 익명클래스 적용
  @Test
  fun `TemplateMethodV2 적용`() {
    val template1 = object: AbstractTemplate() {
      override fun logic() {
        log.info("sub1 business logic ...")
      }
    }
    template1.execute()

    val template2 = object: AbstractTemplate() {
      override fun logic() {
        log.info("sub2 business logic ...")
      }
    }
    template2.execute()
  }
}
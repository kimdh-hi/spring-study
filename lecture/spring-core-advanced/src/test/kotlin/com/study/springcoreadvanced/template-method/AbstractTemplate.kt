package com.study.springcoreadvanced.`template-method`

import org.slf4j.LoggerFactory
import org.springframework.util.StopWatch

/*
템플릿 메서드 직접 구현.
상속관계에서의 다형성을 이용해서 템플릿에서 비즈니스 로직을 호출하는 방식
템플릿 안에서 자식클래스가 재정의한 함수를 호출
 */
abstract class AbstractTemplate {

  private val log = LoggerFactory.getLogger(javaClass)

  abstract fun logic()

  fun execute() {
    val stopWatch = StopWatch()
    stopWatch.start()

    // 비즈니스 로직...
    logic()

    stopWatch.stop()
    log.info("execution time: {}", stopWatch.totalTimeMillis)
  }
}

class SubClass1: AbstractTemplate() {
  private val log = LoggerFactory.getLogger(javaClass)
  override fun logic() {
    Thread.sleep(1000L)
    log.info("sub1 business logic ...")
  }
}

class SubClass2: AbstractTemplate() {
  private val log = LoggerFactory.getLogger(javaClass)
  override fun logic() {
    Thread.sleep(1000L)
    log.info("sub2 business logic ...")
  }
}
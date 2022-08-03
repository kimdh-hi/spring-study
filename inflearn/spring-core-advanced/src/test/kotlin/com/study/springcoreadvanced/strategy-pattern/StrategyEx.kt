package com.study.springcoreadvanced.`strategy-pattern`

import com.study.springcoreadvanced.`strategy-pattern`.strategy.ContextV1
import com.study.springcoreadvanced.`strategy-pattern`.strategy.Strategy
import com.study.springcoreadvanced.`strategy-pattern`.strategy.StrategyLogic1
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.util.StopWatch

/**
 * 템플릿 패턴과 동일하게 부가기능과 주요로직을 분리할 수 있는 디자인 패턴이다.
 * 템플릿 패턴의 경우 상속을 사용하지만 전략패턴은 인터페이스를 통해 위임을 사용한다.
 */
class StrategyEx {

  private val log = LoggerFactory.getLogger(javaClass)

  @Test
  fun `V0`() {
    logic1()
  }

  @Test
  fun `V1`(){
    val strategyLogic1 = StrategyLogic1()
    val contextV1 = ContextV1(strategyLogic1)
    contextV1.execute()
  }

  @Test
  fun `V2 - 익명클래스 - 전략 인터페이스 바로 구현`(){
    val strategyLogic1 = object: Strategy {
      override fun call() {
        Thread.sleep(500L)
        log.info("business logic 1 ...")
      }
    }
    val contextV1 = ContextV1(strategyLogic1)
    contextV1.execute()
  }

  @Test
  fun `V3 - 익명클래스 - 전략 인터페이스 바로 구현`(){
    val contextV1 = ContextV1(object: Strategy {
      override fun call() {
        Thread.sleep(500L)
        log.info("business logic 1 ...")
      }
    })
    contextV1.execute()
  }

  @Test
  fun `V4 - 람다 - kotlin sam`(){
    val contextV1 = ContextV1 {
      Thread.sleep(500L)
      log.info("business logic 1 ...")
    }

    contextV1.execute()
  }

  private fun `logic1`() {
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
}
package com.study.springcoreadvanced.`strategy-pattern`

import com.study.springcoreadvanced.`strategy-pattern`.strategy.ContextV2
import com.study.springcoreadvanced.`strategy-pattern`.strategy.Strategy
import com.study.springcoreadvanced.`strategy-pattern`.strategy.StrategyLogic1
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class ContextV2Test {

  private val log = LoggerFactory.getLogger(javaClass)

  @Test
  fun `V1`() {
    val context = ContextV2()
    context.execute(StrategyLogic1())
  }

  @Test
  fun `V2`() {
    val context = ContextV2()
    context.execute {
      Thread.sleep(500L)
      log.info("business logic 1 ...")
    }
  }
}
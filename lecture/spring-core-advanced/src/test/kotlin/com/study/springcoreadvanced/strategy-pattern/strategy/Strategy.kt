package com.study.springcoreadvanced.`strategy-pattern`.strategy

import org.slf4j.LoggerFactory

fun interface Strategy {
  fun call()
}

class StrategyLogic1: Strategy {
  private val log = LoggerFactory.getLogger(javaClass)
  override fun call() {
    Thread.sleep(500L)
    log.info("business logic 1 ...")
  }
}

class StrategyLogic2: Strategy {
  private val log = LoggerFactory.getLogger(javaClass)
  override fun call() {
    log.info("business logic 2 ...")
  }
}
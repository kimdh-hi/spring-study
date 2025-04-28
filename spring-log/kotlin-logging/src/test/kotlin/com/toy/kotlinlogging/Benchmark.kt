package com.toy.kotlinlogging

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import kotlin.time.measureTime

private val kotlinLogger = KotlinLogging.logger {}

class Benchmark {

  private val logger = LoggerFactory.getLogger(javaClass)

  private val data1 = "data1"
  private val data2 = "data2"
  private val testCount = 100_000

  @Test
  fun parameterizedLogging() {
    val times = mutableListOf<Long>()
    repeat(5) {
      val time = measureTime {
        repeat(testCount) {
          logger.debug("test data1={}, data2={}", data1, data2)
        }
      }.inWholeMilliseconds
      times.add(time)
    }

    println("parameterizedLogging avg: ${times.average()}ms")
  }

  @Test
  fun kotlinLogging() {
    val times = mutableListOf<Long>()
    repeat(10) {
      val time = measureTime {
        repeat(testCount) {
          kotlinLogger.debug { "test data1=$data1 data2=$data2" }
        }
      }.inWholeMilliseconds
      times.add(time)
    }

    println("kotlinLogging avg: ${times.average()}ms")
  }

  @Test
  fun stringTemplateLogging() {
    val times = mutableListOf<Long>()

    repeat(10) {
      val time = measureTime {
        repeat(testCount) {
          logger.debug("test data1=$data1 data2=$data2")
        }
      }.inWholeMilliseconds
      times.add(time)
    }

    println("stringTemplateLogging avg: ${times.average()}ms")
  }
}

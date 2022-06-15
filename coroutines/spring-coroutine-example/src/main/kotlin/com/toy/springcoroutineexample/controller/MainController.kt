package com.toy.springcoroutineexample.controller

import com.toy.springcoroutineexample.service.A_Service
import com.toy.springcoroutineexample.service.B_Service
import com.toy.springcoroutineexample.service.C_Service
import com.toy.springcoroutineexample.service.ConcatService
import com.toy.springcoroutineexample.vo.ConcatRequest
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.util.StopWatch
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class MainController(
  private val aService: A_Service,
  private val bService: B_Service,
  private val cService: C_Service,
  private val concatService: ConcatService) {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping("/coroutine")
  suspend fun coroutine(): String {
    val stopWatch = StopWatch()
    stopWatch.start()

   val concatRequest = runBlocking {
      val resultA = async { aService.executeSuspend() }
      val resultB = async { bService.executeSuspend() }
      val resultC = async { cService.executeSuspend() }

      ConcatRequest(listOf(resultA.await(), resultB.await(), resultC.await()))
    }

    stopWatch.stop()
    log.info("time: {}", stopWatch.totalTimeSeconds)

    return concatService.concate(concatRequest)
  }

  @GetMapping("/normal")
  fun normal(): String {
    val stopWatch = StopWatch()
    stopWatch.start()

    val resultA = aService.executeNormal()
    val resultB = bService.executeNormal()
    val resultC = cService.executeNormal()

    val concatRequest = ConcatRequest(listOf(resultA, resultB, resultC))

    stopWatch.stop()
    log.info("time: {}", stopWatch.totalTimeSeconds)

    return concatService.concate(concatRequest)
  }
}
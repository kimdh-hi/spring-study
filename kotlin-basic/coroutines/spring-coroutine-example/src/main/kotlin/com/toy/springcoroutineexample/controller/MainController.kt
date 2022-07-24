package com.toy.springcoroutineexample.controller

import com.toy.springcoroutineexample.service.A_Service
import com.toy.springcoroutineexample.service.B_Service
import com.toy.springcoroutineexample.service.C_Service
import com.toy.springcoroutineexample.service.ConcatService
import com.toy.springcoroutineexample.vo.ConcatenatedRequestResult
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
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
  suspend fun coroutine(): ResponseEntity<ResponseVO> {
    val stopWatch = StopWatch()
    stopWatch.start()

   val responseVO = coroutineScope {
      val resultA = async { aService.execute() }
      val resultB = async { bService.execute() }
      val resultC = async { cService.execute() }

      ResponseVO(
        dataA = resultA.await(),
        dataB = resultB.await(),
        dataC = resultC.await()
      )
    }

    stopWatch.stop()
    log.info("time: {}", stopWatch.totalTimeSeconds)

    return ResponseEntity.ok(responseVO)
  }

  data class ResponseVO(
    val dataA: String,
    val dataB: String,
    val dataC: String
  )

//  @GetMapping("/reactor")
//  fun reactor(): String {
//    val stopWatch = StopWatch()
//    stopWatch.start()
//
//
//
//    stopWatch.stop()
//    log.info("time: {}", stopWatch.totalTimeSeconds)
//
//    return concatService.concate(concatenatedRequestResult)
//  }

  @GetMapping("/normal")
  suspend fun normal(): String {
    val stopWatch = StopWatch()
    stopWatch.start()

    val resultA = aService.execute()
    val resultB = bService.execute()
    val resultC = cService.execute()

    val concatenatedRequestResult = ConcatenatedRequestResult(listOf(resultA, resultB, resultC))

    stopWatch.stop()
    log.info("time: {}", stopWatch.totalTimeSeconds)

    return concatService.concate(concatenatedRequestResult)
  }
}
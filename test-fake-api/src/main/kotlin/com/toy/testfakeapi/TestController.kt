package com.toy.testfakeapi

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

  private val log = LoggerFactory.getLogger(TestController::class.java)

  @GetMapping("/test1")
  fun test1(@RequestParam status: Int): ResponseEntity<Any> {
    if (status == 400) {
      log.warn("4xx exception ...")
      return ResponseEntity(BaseException("2222"), HttpStatus.BAD_REQUEST)
    } else if (status == 500) {
      log.warn("5xx exception ...")
      return ResponseEntity(BaseException("9999"), HttpStatus.INTERNAL_SERVER_ERROR)
    } else {
      return ResponseEntity.ok("hello")
    }
  }

  @GetMapping("/test2")
  fun test2(@RequestParam status: Int): ResponseEntity<Any> {
    if (status == 400) {
      log.warn("4xx exception ...")
      return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).build()
    } else if (status == 500) {
      log.warn("5xx exception ...")
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build()
    } else {
      return ResponseEntity.ok("hello")
    }
  }

  @GetMapping("/test3")
  fun test3(@RequestParam ids: List<String>): List<String> {
    log.info("ids={}", ids)
    return ids
  }
}

data class TestRequest(
  val data1: String,
  val data2: String,
)

open class BaseException(
  val code: String,
  message: String? = null,
  cause: Throwable? = null
) : RuntimeException(message, cause)

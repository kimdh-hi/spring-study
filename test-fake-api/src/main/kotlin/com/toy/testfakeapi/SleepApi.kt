package com.toy.testfakeapi

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fake/sleeps")
class SleepApi {

  private val log = LoggerFactory.getLogger(SleepApi::class.java)

  @GetMapping
  fun sleep(): ResponseEntity<Unit> {
    log.info("sleep..")
    Thread.sleep(500)
    return ResponseEntity.ok().build()
  }
}

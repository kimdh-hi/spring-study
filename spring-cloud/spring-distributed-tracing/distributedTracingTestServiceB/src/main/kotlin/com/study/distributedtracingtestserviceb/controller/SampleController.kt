package com.study.distributedtracingtestserviceb.controller

import com.study.distributedtracingtestserviceb.service.SampleService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sample")
class SampleController(
  private val sampleService: SampleService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping("/save")
  fun save(@RequestBody request: SaveSampleRequest): String {
    log.info("[info]SampleController.save")
    log.debug("[debug]SampleController.save")
    sampleService.save(request)

    return "ok"
  }

}

data class SaveSampleRequest(
  val data: String
)

package com.study.distributedtracingtestserviceb.service

import com.study.distributedtracingtestserviceb.controller.SaveSampleRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SampleService {

  private val log = LoggerFactory.getLogger(javaClass)

  fun save(request: SaveSampleRequest) {
    log.info("SampleService.save request=$request")
  }
}
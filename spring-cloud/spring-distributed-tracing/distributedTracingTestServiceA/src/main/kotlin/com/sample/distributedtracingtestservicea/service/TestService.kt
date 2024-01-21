package com.sample.distributedtracingtestservicea.service

import com.sample.distributedtracingtestservicea.controller.SampleSaveRequest
import com.sample.distributedtracingtestservicea.feign.BServiceClient
import com.sample.distributedtracingtestservicea.feign.BServiceSaveRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TestService(
  private val bServiceClient: BServiceClient
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun save(request: SampleSaveRequest) {
    log.info("[info]TestService.save request=$request")
    log.debug("[debug]TestService.save request=$request")

    bServiceClient.save(BServiceSaveRequest(request.bData))
  }
}
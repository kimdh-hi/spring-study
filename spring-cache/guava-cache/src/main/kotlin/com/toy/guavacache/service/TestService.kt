package com.toy.guavacache.service

import com.toy.guavacache.domain.TestData
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TestService {

  private val log = LoggerFactory.getLogger(javaClass)

  fun get(id: String): TestData {
    log.info("service called")
    return TestData(id = id, data = "data")
  }
}
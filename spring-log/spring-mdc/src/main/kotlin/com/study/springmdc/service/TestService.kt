package com.study.springmdc.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.Thread.sleep

@Service
class TestService {

  private val log = LoggerFactory.getLogger(javaClass)

  fun service() {
    log.info("service start...")
    sleep(500)
    log.info("service end...")
  }
}
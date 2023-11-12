package com.toy.springaop.internalcall

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class CallServiceV3(
  private val internalService: InternalService
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun external() {
    log.info("external...")
    internalService.internal()
  }

}

@Component
class InternalService {

  private val log = LoggerFactory.getLogger(javaClass)
  fun internal() {
    log.info("internal...")
  }
}
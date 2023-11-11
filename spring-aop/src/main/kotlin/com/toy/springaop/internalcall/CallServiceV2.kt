package com.toy.springaop.internalcall

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class CallServiceV2(
  private val applicationContext: ApplicationContext,
  private val callServiceProvider: ObjectProvider<CallServiceV2>
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun external() {
    log.info("external...")
//    applicationContext.getBean(CallServiceV2::class.java).internal()
    callServiceProvider.getObject().internal()
  }

  fun internal() {
    log.info("internal...")
  }
}
package com.toy.springcore.event

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class EventTestAppRunner(
  private val applicationContext: ApplicationContext
): ApplicationRunner {
  override fun run(args: ApplicationArguments) {
    val myEvent = MyEvent("data!!!")
    applicationContext.publishEvent(myEvent)
  }
}
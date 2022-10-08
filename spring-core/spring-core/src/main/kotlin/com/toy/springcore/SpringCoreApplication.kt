package com.toy.springcore

import com.toy.springcore.aop.EventService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.stereotype.Component

@SpringBootApplication
class SpringCoreApplication

fun main(args: Array<String>) {
  runApplication<SpringCoreApplication>(*args)
}

@Component
class AppRunner(
  private val eventService: EventService
): ApplicationRunner {
  override fun run(args: ApplicationArguments?) {
    eventService.createEvent()
    eventService.publishEvent()
    eventService.deleteEvent()
  }
}

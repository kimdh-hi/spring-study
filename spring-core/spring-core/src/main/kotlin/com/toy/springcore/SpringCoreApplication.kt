package com.toy.springcore

import com.toy.springcore.event.MyApplicationStartingEventListener
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class SpringCoreApplication

fun main(args: Array<String>) {
  runApplication<SpringCoreApplication>(*args)
}

//@Component
//class AppRunner(
//  private val eventService: EventService
//): ApplicationRunner {
//  override fun run(args: ApplicationArguments?) {
//    eventService.createEvent()
//    eventService.publishEvent()
//    eventService.deleteEvent()
//  }
//}

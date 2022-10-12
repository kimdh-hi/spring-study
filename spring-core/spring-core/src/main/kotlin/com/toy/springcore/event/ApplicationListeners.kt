package com.toy.springcore.event

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.context.event.ApplicationStartingEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

// ApplicationContext 도 만들어지기 전 애플리케이션 구동 시 발생하는 이벤트이기 때문에 빈으로 등록해도 이벤트에 대한 처리를 하지 못한다.
// 직접 등록해줘야 한다
/**
val springCoreApplication = SpringApplication(SpringCoreApplication::class.java)
springCoreApplication.addListeners(MyApplicationStartingEventListener())
springCoreApplication.run(*args)
 */
//@Component
class MyApplicationStartingEventListener: ApplicationListener<ApplicationStartingEvent> {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun onApplicationEvent(event: ApplicationStartingEvent) {
    println("[ApplicationStartingEvent...]")
  }
}

// application-context 생성 후 발생하는 이벤트이므로 잡을 수 리스너로 이벤트 수신 가능
@Component
class MyApplicationStartedEventListener: ApplicationListener<ApplicationStartedEvent> {
  override fun onApplicationEvent(event: ApplicationStartedEvent) {
    println("[ApplicationStartedEvent...]")
  }
}

// VM options=-Dfoo
// Program arguments=--bar ==> ApplicationArguments
@Component
class MyArgumentListener{
  constructor(arguments: ApplicationArguments) {
    println("foo: ${arguments.containsOption("foo")}")
    println("bar: ${arguments.containsOption("bar")}")
  }
}
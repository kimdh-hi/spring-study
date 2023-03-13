package com.toy.springhttpinterface

import com.toy.springhttpinterface.httpinterface.FakeApiCaller
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Runner {

  @Bean
  fun run(fakeApiCaller: FakeApiCaller) = ApplicationRunner {
    val result = fakeApiCaller.getUsers("1")
    println(result)
  }
}
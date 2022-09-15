package com.lecture.divelog

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class DivelogApplication

fun main(args: Array<String>) {
  args.forEach {
    println("ProgramArgument: $it")
  }
  runApplication<DivelogApplication>(*args) {
    setBannerMode(Banner.Mode.OFF)
  }
}

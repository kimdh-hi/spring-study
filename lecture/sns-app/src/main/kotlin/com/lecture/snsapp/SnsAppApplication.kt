package com.lecture.snsapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
class SnsAppApplication

fun main(args: Array<String>) {
  runApplication<SnsAppApplication>(*args)
}

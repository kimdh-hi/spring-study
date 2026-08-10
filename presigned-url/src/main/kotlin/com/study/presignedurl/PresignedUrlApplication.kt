package com.study.presignedurl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PresignedUrlApplication

fun main(args: Array<String>) {
  runApplication<PresignedUrlApplication>(*args)
}

package com.study.archunit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ArchUnitApplication

fun main(args: Array<String>) {
  runApplication<ArchUnitApplication>(*args)
}

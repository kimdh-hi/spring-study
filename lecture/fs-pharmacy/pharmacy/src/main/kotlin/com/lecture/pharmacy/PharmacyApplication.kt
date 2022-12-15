package com.lecture.pharmacy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class PharmacyApplication

fun main(args: Array<String>) {
  runApplication<PharmacyApplication>(*args)
}

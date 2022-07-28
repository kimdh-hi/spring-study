package com.toy.hibernatelisteners

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
class HibernateListenersApplication

fun main(args: Array<String>) {
  runApplication<HibernateListenersApplication>(*args)
}

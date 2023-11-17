package com.toy.serviceb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServiceBApplication

fun main(args: Array<String>) {
	runApplication<ServiceBApplication>(*args)
}

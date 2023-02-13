package com.toy.producer

import com.toy.producer.producer.`04-retry`.RetryTestProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
//@EnableScheduling
class ProducerApplication(
	private val retryTestProducer: RetryTestProducer
): CommandLineRunner {
	override fun run(vararg args: String) {
		retryTestProducer.send("test")
	}
}

fun main(args: Array<String>) {
	runApplication<ProducerApplication>(*args)
}

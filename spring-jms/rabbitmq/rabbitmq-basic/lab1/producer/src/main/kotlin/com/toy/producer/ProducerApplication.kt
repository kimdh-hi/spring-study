package com.toy.producer

import com.toy.producer.producer.HelloProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProducerApplication(
	private val helloProducer: HelloProducer
): CommandLineRunner {
	override fun run(vararg args: String?) {
		helloProducer.send("hello~~")
	}
}

fun main(args: Array<String>) {
	runApplication<ProducerApplication>(*args)
}

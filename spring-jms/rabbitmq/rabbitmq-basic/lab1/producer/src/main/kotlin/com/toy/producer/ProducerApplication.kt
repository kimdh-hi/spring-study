package com.toy.producer

import com.toy.producer.domain.User
import com.toy.producer.producer.exchange.`01-fanout`.FanoutProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDateTime

@SpringBootApplication
//@EnableScheduling
class ProducerApplication(
	private val producer: FanoutProducer
): CommandLineRunner {
	override fun run(vararg args: String?) {
		for (i in 1..10) {
			producer.sendMessage(User("u-$i", "name$i", LocalDateTime.now()))
		}
	}
}

fun main(args: Array<String>) {
	runApplication<ProducerApplication>(*args)
}

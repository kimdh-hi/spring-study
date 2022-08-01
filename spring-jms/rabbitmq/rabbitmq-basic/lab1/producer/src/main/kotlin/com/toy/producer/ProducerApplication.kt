package com.toy.producer

import com.toy.producer.domain.User
import com.toy.producer.domain.UserRole
import com.toy.producer.producer.exchange.`01-fanout`.FanoutProducer
import com.toy.producer.producer.exchange.`02-direct`.DirectProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDateTime

@SpringBootApplication
//@EnableScheduling
class ProducerApplication(
	private val producer: DirectProducer
): CommandLineRunner {
	override fun run(vararg args: String?) {
		for (i in 1..5) {
			producer.sendMessage(User("u-$i", "name$i", LocalDateTime.now(), UserRole.ADMIN))
		}

		for (i in 1..5) {
			producer.sendMessage(User("u-$i", "name$i", LocalDateTime.now(), UserRole.USER))
		}
	}
}

fun main(args: Array<String>) {
	runApplication<ProducerApplication>(*args)
}

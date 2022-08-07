package com.toy.producer

import com.toy.producer.domain.Furniture
import com.toy.producer.domain.User
import com.toy.producer.domain.UserRole
import com.toy.producer.producer.`03-exchange`.`02-direct`.DirectProducer
import com.toy.producer.producer.`03-exchange`.`04-header`.HeaderProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDateTime

@SpringBootApplication
//@EnableScheduling
class ProducerApplication(
	private val producer: HeaderProducer
): CommandLineRunner {
	override fun run(vararg args: String?) {
		val colors = listOf("white", "red", "green")
		val materials = listOf("wood", "plastic", "steel")

		for (i in 0..10) {
			val furniture = Furniture(name = "f$i", price = i,
				color = colors[i%colors.size],
				material = materials[i%materials.size])
			producer.sendMessage(furniture)
		}
	}
}

fun main(args: Array<String>) {
	runApplication<ProducerApplication>(*args)
}

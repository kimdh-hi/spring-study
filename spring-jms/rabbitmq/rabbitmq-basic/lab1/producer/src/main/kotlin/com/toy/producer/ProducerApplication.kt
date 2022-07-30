package com.toy.producer

import com.toy.producer.domain.Employee
import com.toy.producer.producer.`02-json-producer`.EmployeeJsonProducer
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.LocalDateTime

@SpringBootApplication
//@EnableScheduling
class ProducerApplication(
	private val employeeJsonProducer: EmployeeJsonProducer
): CommandLineRunner {
	override fun run(vararg args: String?) {
		employeeJsonProducer.sendMessage(Employee("e1", "name", LocalDateTime.now()))
	}
}

fun main(args: Array<String>) {
	runApplication<ProducerApplication>(*args)
}

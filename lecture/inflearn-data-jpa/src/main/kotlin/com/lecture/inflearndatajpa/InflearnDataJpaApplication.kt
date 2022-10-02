package com.lecture.inflearndatajpa

import com.lecture.inflearndatajpa.domain.Account
import com.lecture.inflearndatajpa.domain.Address
import org.hibernate.Session
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootApplication
class InflearnDataJpaApplication

fun main(args: Array<String>) {
	runApplication<InflearnDataJpaApplication>(*args)
}

@Component
@Transactional
class JpaRunner(
	@PersistenceContext
	private val entityManager: EntityManager
): ApplicationRunner {
	override fun run(args: ApplicationArguments) {
		val account = Account.of(
			username = "kim",
			password = "password",
			address = Address("street1", "city1", "state1", "zipCode1"),
		)
//		entityManager.persist(account)

		val session = entityManager.unwrap(Session::class.java)
		val savedAccount = session.save(account)
		println(savedAccount)
	}
}

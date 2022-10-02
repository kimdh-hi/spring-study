package com.lecture.inflearndatajpa

import com.lecture.inflearndatajpa.domain.Account
import com.lecture.inflearndatajpa.domain.Address
import com.lecture.inflearndatajpa.domain.Study
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

		val account = Account(
			username = "kim",
			password = "password",
			address = Address("street1", "city1", "state1", "zipCode1"),
		)

		val jpaStudy = Study(name = "jpa")

		// 양방향 관계에서 관계의 주인이 아닌 쪽에 관계가 설정된다면 이 관계는 무시된다.
		// (study 의 FK 역할을 할 owner_id 가 null 로 들어간다
//		account.studies.add(jpaStudy)

		jpaStudy.addOwner(account)

		val session = entityManager.unwrap(Session::class.java)
		session.save(account)
		session.save(jpaStudy)
	}
}

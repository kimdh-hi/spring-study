package com.lecture.inflearndatajpa

import com.lecture.inflearndatajpa.domain.*
import org.hibernate.Session
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootApplication
//@EnableJpaRepositories(repositoryImplementationPostfix = "SomePostfix") // customRepository 구현체 클래스 네이밍 룰 default=Impl
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
		val post = Post(title = "title")
		val comment = Comment(comment = "comment")
		post.addComment(comment)

		val session = entityManager.unwrap(Session::class.java)
		session.save(post)
	}
}

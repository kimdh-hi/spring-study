package com.toy.jpainheritance

import com.toy.jpainheritance.domain.Album
import com.toy.jpainheritance.domain.Book
import com.toy.jpainheritance.repository.BookRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootApplication
class JpaInheritanceApplication

fun main(args: Array<String>) {
  runApplication<JpaInheritanceApplication>(*args)
}

@Configuration
class AppConfig {
  @Bean
  @Transactional
  fun applicationRunner(
    bookRepository: BookRepository
  ) = ApplicationRunner {
    val book = Book(author = "author", isbn = "1111", name = "kim", price = 1000)
    bookRepository.save(book)
  }
}
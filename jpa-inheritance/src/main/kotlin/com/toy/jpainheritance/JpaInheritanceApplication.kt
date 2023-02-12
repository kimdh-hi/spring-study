package com.toy.jpainheritance

import com.toy.jpainheritance.domain.Album
import com.toy.jpainheritance.domain.Book
import com.toy.jpainheritance.domain.Movie
import com.toy.jpainheritance.repository.AlbumRepository
import com.toy.jpainheritance.repository.BookRepository
import com.toy.jpainheritance.repository.MovieRepository
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
    bookRepository: BookRepository,
    albumRepository: AlbumRepository,
    movieRepository: MovieRepository
  ) = ApplicationRunner {
    val book = Book(author = "author", isbn = "1111", name = "kim", price = 1000)
    bookRepository.save(book)

    val album = Album(artist = "artist", name = "lee", price = 5000)
    albumRepository.save(album)

    val movie = Movie(director = "director", actor = "actor", name = "park", price = 10000)
    movieRepository.save(movie)
  }
}
package com.toy.springjooq.film

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FilmRepositoryTest @Autowired constructor(
  private val fileRepository: FilmRepository
) {

  @Test
  fun findById() {
    val film = fileRepository.findById(1L)
    assertThat(film).isNotNull
  }

  @Test
  fun findByIdV2() {
    val film = fileRepository.findByIdV2(1L)
    println(film)
    assertAll(
      { assertThat(film).isNotNull },
      { assertThat(film).hasNoNullFieldsOrProperties() },
    )
  }

  @Test
  fun findAllWithActors() {
    val films = fileRepository.findAllWithActors(0, 0)
    films.forEach {
      println(it)
    }
  }
}

package com.toy.springjooq.film

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
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
    assertThat(film).isNotNull
  }
}

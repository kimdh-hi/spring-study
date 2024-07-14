package com.toy.springjooq.film

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
    println(film)
  }

}

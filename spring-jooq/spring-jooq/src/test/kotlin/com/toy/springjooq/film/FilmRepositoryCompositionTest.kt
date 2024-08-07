package com.toy.springjooq.film

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FilmRepositoryCompositionTest @Autowired constructor(
  private val filmRepositoryComposition: FilmRepositoryComposition
) {
  @Test
  fun findById() {
    val film = filmRepositoryComposition.findById(1L)
    assertNotNull(film)
  }
}

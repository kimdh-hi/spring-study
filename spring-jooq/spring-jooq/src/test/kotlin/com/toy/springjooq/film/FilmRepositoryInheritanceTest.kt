package com.toy.springjooq.film

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FilmRepositoryInheritanceTest @Autowired constructor(
  private val filmRepositoryInheritance: FilmRepositoryInheritance
) {

  @Test
  fun findById() {
    val film = filmRepositoryInheritance.findById(1L)
    assertNotNull(film)
    println(film)
  }
}

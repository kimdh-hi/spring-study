package com.lecture.snsapp.repository

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PostRepositoryTest(
  private val postRepository: PostRepository
) {

  @Test
  fun findById() {
    assertNotNull(postRepository.findByIdOrNull("post-01"))
  }
}
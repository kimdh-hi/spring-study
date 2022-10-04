package com.lecture.inflearndatajpa.repository

import com.lecture.inflearndatajpa.domain.Comment
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor
import org.springframework.util.concurrent.ListenableFuture

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CommentRepositoryTest(
  private val commentRepository: CommentRepository
) {

  @Test
  fun saveAndFindAll() {
    //given
    val comment = Comment(comment = "comment")

    //when
    val savedComment = commentRepository.save(comment)

    //then
    assertNotNull(savedComment.id)

    //when
    val comments = commentRepository.findAll()

    //then
    assertEquals(1, comments.size)
  }
}
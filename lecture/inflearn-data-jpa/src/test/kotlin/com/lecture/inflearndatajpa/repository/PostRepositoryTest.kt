package com.lecture.inflearndatajpa.repository

import com.lecture.inflearndatajpa.domain.Post
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.TestConstructor

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PostRepositoryTest(
  private val postRepository: PostRepository
) {

  @Test
  fun jpaRepositoryTest() {
    //given
    val post = Post(title = "title")

    //when
    val savedPost = postRepository.save(post)
    //then
    assertNotNull(savedPost.id)

    //when
    val posts = postRepository.findAll()
    //then
    assertEquals(1, posts.size)

    //when
    val pagePosts = postRepository.findAll(PageRequest.of(0, 10))
    //then
    assertEquals(1, pagePosts.content.size)
    assertEquals(1, pagePosts.totalElements)

    //when
    val searchPagePosts = postRepository.findByTitleContains("title", PageRequest.of(0, 10))
    //then
    assertEquals(1, searchPagePosts.content.size)
    assertEquals(1, searchPagePosts .totalElements)

    //when
    val count = postRepository.countByTitleContains("title")
    //then
    assertEquals(1, count)
  }

  @Test
  fun customRepositoryTest() {
    //given
    val post = Post(title = "title")
    val savedPost = postRepository.save(post)

    //when
    val posts = postRepository.findPost()
    //then
    assertEquals(savedPost.id, posts[0].id)

    //when
    postRepository.delete(savedPost)
    postRepository.flush()
  }
}
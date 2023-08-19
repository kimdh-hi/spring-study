package com.toy.jpaeventlistener.domain.event

import com.toy.jpaeventlistener.domain.Post
import com.toy.jpaeventlistener.repository.PostRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest
@Transactional
class PostPublishedEventTest @Autowired constructor(
  private val postRepository: PostRepository,
  private val applicationContext: ApplicationContext
) {

  @Test
  fun `test`() {
    val post = Post(title = "title")
    val postPublishedEvent = PostPublishedEvent(post)
    applicationContext.publishEvent(postPublishedEvent)
  }
}
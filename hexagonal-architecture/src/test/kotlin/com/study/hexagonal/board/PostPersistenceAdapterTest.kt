package com.study.hexagonal.board

import com.study.hexagonal.board.adapter.outbound.persistence.PostPersistenceAdapter
import com.study.hexagonal.board.application.domain.Post
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.context.annotation.Import
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DataJpaTest
@Import(PostPersistenceAdapter::class)
class PostPersistenceAdapterTest(
  @Autowired private val adapter: PostPersistenceAdapter
) {

  @Test
  fun `게시글과 댓글이 도메인 모델로 왕복 매핑된다`() {
    val saved = adapter.save(Post(authorId = 1, title = "제목", content = "본문"))
    val commented = adapter.save(saved.addComment(authorId = 2, content = "댓글"))

    val found = adapter.findById(commented.id!!)!!

    assertEquals("제목", found.title)
    assertEquals(1, found.comments.size)
    assertEquals("댓글", found.comments.first().content)
    assertEquals(2, found.comments.first().authorId)
  }

  @Test
  fun `게시글을 삭제하면 댓글도 함께 삭제된다`() {
    val post = adapter.save(Post(authorId = 1, title = "제목", content = "본문"))
    val commented = adapter.save(post.addComment(authorId = 2, content = "댓글"))

    adapter.delete(commented)

    assertNull(adapter.findById(commented.id!!))
  }

  @Test
  fun `도메인에서 제거한 댓글은 삭제된다`() {
    val post = adapter.save(Post(authorId = 1, title = "제목", content = "본문"))
    val commented = adapter.save(post.addComment(authorId = 2, content = "댓글"))
    val commentId = commented.comments.first().id!!

    adapter.save(commented.removeComment(commentId, requesterId = 1))

    assertTrue(adapter.findById(commented.id!!)!!.comments.isEmpty())
  }
}

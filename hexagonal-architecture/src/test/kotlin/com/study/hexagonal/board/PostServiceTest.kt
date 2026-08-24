package com.study.hexagonal.board

import com.study.hexagonal.board.application.AuthorService
import com.study.hexagonal.board.application.PostService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PostServiceTest {

  private val authorRepository = FakeAuthorRepository()
  private val authorService = AuthorService(authorRepository)
  private val postService = PostService(FakePostRepository(), authorRepository)

  private val writer = authorService.register("daehyun", "writer@example.com").id!!
  private val reader = authorService.register("reader", "reader@example.com").id!!

  @Test
  fun `게시글 작성 후 조회`() {
    val post = postService.write(writer, "제목", "본문")
    assertEquals(post, postService.find(post.id!!))
  }

  @Test
  fun `존재하지 않는 작성자는 글을 쓸 수 없다`() {
    assertFailsWith<IllegalArgumentException> { postService.write(999, "제목", "본문") }
  }

  @Test
  fun `게시글은 작성자만 수정할 수 있다`() {
    val postId = postService.write(writer, "제목", "본문").id!!

    assertEquals("수정 제목", postService.edit(postId, writer, "수정 제목", "수정 본문").title)
    assertFailsWith<IllegalArgumentException> { postService.edit(postId, reader, "탈취", "탈취") }
  }

  @Test
  fun `댓글 작성 후 목록에 담긴다`() {
    val postId = postService.write(writer, "제목", "본문").id!!

    val commented = postService.comment(postId, reader, "첫 댓글")

    assertEquals(1, commented.comments.size)
    assertEquals("첫 댓글", commented.comments.first().content)
  }

  @Test
  fun `댓글은 댓글 작성자나 게시글 작성자만 삭제할 수 있다`() {
    val postId = postService.write(writer, "제목", "본문").id!!
    val commentId = postService.comment(postId, reader, "댓글").comments.first().id!!
    val stranger = authorService.register("stranger", "stranger@example.com").id!!

    assertFailsWith<IllegalArgumentException> { postService.deleteComment(postId, commentId, stranger) }
    assertTrue(postService.deleteComment(postId, commentId, writer).comments.isEmpty())
  }

  @Test
  fun `게시글은 작성자만 삭제할 수 있다`() {
    val postId = postService.write(writer, "제목", "본문").id!!

    assertFailsWith<IllegalArgumentException> { postService.delete(postId, reader) }
    postService.delete(postId, writer)
    assertFailsWith<NoSuchElementException> { postService.find(postId) }
  }

  @Test
  fun `없는 게시글 조회 실패`() {
    assertFailsWith<NoSuchElementException> { postService.find(999) }
  }

  @Test
  fun `제목이 비어 있으면 작성 실패`() {
    assertFailsWith<IllegalArgumentException> { postService.write(writer, " ", "본문") }
  }
}

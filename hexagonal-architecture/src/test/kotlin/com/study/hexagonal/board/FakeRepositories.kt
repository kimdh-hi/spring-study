package com.study.hexagonal.board

import com.study.hexagonal.board.application.port.outbound.AuthorRepository
import com.study.hexagonal.board.application.port.outbound.PostRepository
import com.study.hexagonal.board.application.domain.Author
import com.study.hexagonal.board.application.domain.Post

class FakeAuthorRepository : AuthorRepository {
  private val store = mutableMapOf<Long, Author>()
  private var sequence = 0L

  override fun save(author: Author): Author {
    val saved = author.copy(id = author.id ?: ++sequence)
    store[saved.id!!] = saved
    return saved
  }

  override fun findById(authorId: Long): Author? = store[authorId]

  override fun existsById(authorId: Long): Boolean = store.containsKey(authorId)

  override fun existsByEmail(email: String): Boolean = store.values.any { it.email == email }
}

class FakePostRepository : PostRepository {
  private val store = mutableMapOf<Long, Post>()
  private var postSequence = 0L
  private var commentSequence = 0L

  override fun save(post: Post): Post {
    val saved = post.copy(
      id = post.id ?: ++postSequence,
      comments = post.comments.map { if (it.id == null) it.copy(id = ++commentSequence) else it }
    )
    store[saved.id!!] = saved
    return saved
  }

  override fun delete(post: Post) {
    store.remove(post.id)
  }

  override fun findById(postId: Long): Post? = store[postId]

  override fun findAll(): List<Post> = store.values.sortedBy { it.id }
}

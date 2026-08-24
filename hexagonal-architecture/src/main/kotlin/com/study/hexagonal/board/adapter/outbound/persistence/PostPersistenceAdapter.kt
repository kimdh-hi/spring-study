package com.study.hexagonal.board.adapter.outbound.persistence

import com.study.hexagonal.board.application.port.outbound.PostRepository
import com.study.hexagonal.board.application.domain.Comment
import com.study.hexagonal.board.application.domain.Post
import org.springframework.stereotype.Repository

@Repository
class PostPersistenceAdapter(
  private val jpaRepository: PostJpaRepository
) : PostRepository {

  override fun save(post: Post): Post = jpaRepository.save(post.toEntity()).toDomain()

  override fun delete(post: Post) = jpaRepository.deleteById(post.id!!)

  override fun findById(postId: Long): Post? =
    jpaRepository.findById(postId).map { it.toDomain() }.orElse(null)

  override fun findAll(): List<Post> = jpaRepository.findAll().map { it.toDomain() }
}

private fun Post.toEntity() = PostJpaEntity(
  id = id,
  authorId = authorId,
  title = title,
  content = content,
  comments = comments.map { CommentJpaEntity(it.id, it.authorId, it.content) }.toMutableList()
)

private fun PostJpaEntity.toDomain() = Post(
  id = id,
  authorId = authorId,
  title = title,
  content = content,
  comments = comments.map { Comment(it.id, it.authorId, it.content) }
)

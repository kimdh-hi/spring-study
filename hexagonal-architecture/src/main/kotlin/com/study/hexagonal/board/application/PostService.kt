package com.study.hexagonal.board.application

import com.study.hexagonal.board.application.port.inbound.PostQuery
import com.study.hexagonal.board.application.port.inbound.PostUseCase
import com.study.hexagonal.board.application.port.outbound.AuthorRepository
import com.study.hexagonal.board.application.port.outbound.PostRepository
import com.study.hexagonal.board.application.domain.Post
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostService(
  private val postRepository: PostRepository,
  private val authorRepository: AuthorRepository
) : PostUseCase, PostQuery {

  override fun write(authorId: Long, title: String, content: String): Post {
    requireAuthor(authorId)
    return postRepository.save(Post(authorId = authorId, title = title, content = content))
  }

  override fun edit(postId: Long, editorId: Long, title: String, content: String): Post =
    postRepository.save(find(postId).edit(editorId, title, content))

  override fun delete(postId: Long, requesterId: Long) {
    val post = find(postId)
    post.ensureDeletableBy(requesterId)
    postRepository.delete(post)
  }

  override fun comment(postId: Long, authorId: Long, content: String): Post {
    requireAuthor(authorId)
    return postRepository.save(find(postId).addComment(authorId, content))
  }

  override fun deleteComment(postId: Long, commentId: Long, requesterId: Long): Post =
    postRepository.save(find(postId).removeComment(commentId, requesterId))

  @Transactional(readOnly = true)
  override fun find(postId: Long): Post =
    postRepository.findById(postId) ?: throw NoSuchElementException("게시글을 찾을 수 없습니다: $postId")

  @Transactional(readOnly = true)
  override fun findAll(): List<Post> = postRepository.findAll()

  private fun requireAuthor(authorId: Long) {
    require(authorRepository.existsById(authorId)) { "존재하지 않는 작성자입니다: $authorId" }
  }
}

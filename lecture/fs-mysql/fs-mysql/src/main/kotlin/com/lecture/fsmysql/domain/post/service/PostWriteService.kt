package com.lecture.fsmysql.domain.post.service

import com.lecture.fsmysql.domain.post.dto.PostCommand
import com.lecture.fsmysql.domain.post.entity.Post
import com.lecture.fsmysql.domain.post.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class PostWriteService(
  private val postRepository: PostRepository
) {

  fun create(command: PostCommand): Long {
    val post = Post(
      memberId = command.memberId,
      content = command.content
    )
    return postRepository.save(post).id!!
  }
}
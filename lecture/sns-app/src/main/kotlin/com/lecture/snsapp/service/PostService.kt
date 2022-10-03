package com.lecture.snsapp.service

import com.lecture.snsapp.domain.Post
import com.lecture.snsapp.exception.ApplicationException
import com.lecture.snsapp.exception.ErrorCode
import com.lecture.snsapp.repository.PostRepository
import com.lecture.snsapp.repository.UserRepository
import com.lecture.snsapp.vo.PostCreateResponseVO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
  private val postRepository: PostRepository,
  private val userRepository: UserRepository
) {

  @Transactional
  fun create(title: String, body: String, username: String): PostCreateResponseVO {
    val user = userRepository.findByUsername(username)
      ?: throw ApplicationException(ErrorCode.USER_NOT_FOUND)
    val post = Post.of(title, body, user)
    val savedPost = postRepository.save(post)
    return PostCreateResponseVO.of(savedPost)
  }
}
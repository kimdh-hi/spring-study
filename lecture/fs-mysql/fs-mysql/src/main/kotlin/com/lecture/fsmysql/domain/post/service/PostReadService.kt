package com.lecture.fsmysql.domain.post.service

import com.lecture.fsmysql.domain.post.dto.DailyPostCountRequestDto
import com.lecture.fsmysql.domain.post.dto.DailyPostCountResponseDto
import com.lecture.fsmysql.domain.post.entity.Post
import com.lecture.fsmysql.domain.post.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class PostReadService(
  private val postRepository: PostRepository
) {

  fun getDailyPostCount(requestDto: DailyPostCountRequestDto): List<DailyPostCountResponseDto> {
    return postRepository.groupByCreatedDate(requestDto)
  }
}
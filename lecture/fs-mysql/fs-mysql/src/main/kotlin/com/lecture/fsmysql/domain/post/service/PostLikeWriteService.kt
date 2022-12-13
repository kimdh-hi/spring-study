package com.lecture.fsmysql.domain.post.service

import com.lecture.fsmysql.domain.member.dto.MemberDto
import com.lecture.fsmysql.domain.post.dto.PostCommand
import com.lecture.fsmysql.domain.post.entity.Post
import com.lecture.fsmysql.domain.post.entity.PostLike
import com.lecture.fsmysql.domain.post.repository.PostLikeRepository
import com.lecture.fsmysql.domain.post.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostLikeWriteService(
  private val postLikeRepository: PostLikeRepository
) {

  fun create(post: Post, memberDto: MemberDto): Long {
    val postLike = PostLike(
      memberId = memberDto.id,
      postId = post.id
    )
    return postLikeRepository.save(postLike).id
  }
}
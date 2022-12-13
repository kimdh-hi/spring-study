package com.lecture.fsmysql.application.usecase

import com.lecture.fsmysql.domain.member.service.MemberReadService
import com.lecture.fsmysql.domain.post.service.PostLikeWriteService
import com.lecture.fsmysql.domain.post.service.PostReadService
import org.springframework.stereotype.Service

@Service
class CreatePostLikeUsecase(
  private val postReadService: PostReadService,
  private val memberReadService: MemberReadService,
  private val postLikeWriteService: PostLikeWriteService
) {

  fun execute(postId: Long, memberId: Long) {
    val post = postReadService.getPost(postId)
    val memberDto = memberReadService.getMember(memberId)
    postLikeWriteService.create(post, memberDto)
  }
}
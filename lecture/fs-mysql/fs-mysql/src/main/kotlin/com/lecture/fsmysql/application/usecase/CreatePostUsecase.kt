package com.lecture.fsmysql.application.usecase

import com.lecture.fsmysql.domain.follow.service.FollowReadService
import com.lecture.fsmysql.domain.post.dto.PostCommand
import com.lecture.fsmysql.domain.post.service.PostWriteService
import com.lecture.fsmysql.domain.post.service.TimelineWriteService
import org.springframework.stereotype.Component

@Component
class CreatePostUsecase(
  private val postWriteService: PostWriteService,
  private val followReadService: FollowReadService,
  private val timelineWriteService: TimelineWriteService
) {

  fun execute(postCommand: PostCommand): Long {
    val postId = postWriteService.create(postCommand)
    val followers = followReadService.getFollowers(postCommand.memberId)
    timelineWriteService.devliveryToTimeline(postId, followers.map { it.fromMemberId })
    return postId
  }
}
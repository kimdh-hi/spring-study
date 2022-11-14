package com.lecture.fsmysql.application.usecase

import com.lecture.fsmysql.common.CursorRequest
import com.lecture.fsmysql.common.PageCursor
import com.lecture.fsmysql.domain.follow.service.FollowReadService
import com.lecture.fsmysql.domain.post.entity.Post
import com.lecture.fsmysql.domain.post.service.PostReadService
import com.lecture.fsmysql.domain.post.service.TimelineReadService
import org.springframework.stereotype.Component

@Component
class GetTimelinePostsUsecase(
  private val followReadService: FollowReadService,
  private val postReadService: PostReadService,
  private val timelineReadService: TimelineReadService
) {

  /**
   * 내가 팔로우하고 있는 사용자의 게시글 조회
   */
  fun execute(memberId: Long, cursorRequest: CursorRequest): PageCursor<Post> {
    val followingIds = followReadService.getFollowings(memberId)
      .map { it.toMemberId }
    return postReadService.getPosts(followingIds, cursorRequest)
  }

  fun executeByTimeline(memberId: Long, cursorRequest: CursorRequest): PageCursor<Post> {
    val timelines = timelineReadService.getTimelines(memberId, cursorRequest)
    val postIds = timelines.body.map { it.postId }
    val posts = postReadService.getPosts(postIds)
    return PageCursor(timelines.nextCursorRequest, posts)
  }
}
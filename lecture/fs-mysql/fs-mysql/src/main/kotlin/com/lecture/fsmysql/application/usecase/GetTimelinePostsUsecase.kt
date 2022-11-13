package com.lecture.fsmysql.application.usecase

import com.lecture.fsmysql.common.CursorRequest
import com.lecture.fsmysql.common.PageCursor
import com.lecture.fsmysql.domain.follow.service.FollowReadService
import com.lecture.fsmysql.domain.post.entity.Post
import com.lecture.fsmysql.domain.post.service.PostReadService
import org.springframework.stereotype.Component

@Component
class GetTimelinePostsUsecase(
  private val followReadService: FollowReadService,
  private val postReadService: PostReadService
) {

  /**
   * 내가 팔로우하고 있는 사용자의 게시글 조회
   */
  fun exuecte(memberId: Long, cursorRequest: CursorRequest): PageCursor<Post> {
    val followingIds = followReadService.getFollowings(memberId)
      .map { it.toMemberId }
    return postReadService.getPosts(followingIds, cursorRequest)
  }
}
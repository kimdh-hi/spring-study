package com.lecture.fsmysql.application.controller

import com.lecture.fsmysql.application.usecase.CreatePostLikeUsecase
import com.lecture.fsmysql.application.usecase.CreatePostUsecase
import com.lecture.fsmysql.application.usecase.GetTimelinePostsUsecase
import com.lecture.fsmysql.common.CursorRequest
import com.lecture.fsmysql.common.PageCursor
import com.lecture.fsmysql.domain.post.dto.DailyPostCountRequestDto
import com.lecture.fsmysql.domain.post.dto.DailyPostCountResponseDto
import com.lecture.fsmysql.domain.post.dto.PostCommand
import com.lecture.fsmysql.domain.post.dto.PostDto
import com.lecture.fsmysql.domain.post.entity.Post
import com.lecture.fsmysql.domain.post.service.PostReadService
import com.lecture.fsmysql.domain.post.service.PostWriteService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
  private val postWriteService: PostWriteService,
  private val postReadService: PostReadService,
  private val getTimelinePostsUsecase: GetTimelinePostsUsecase,
  private val createPostUsecase: CreatePostUsecase,
  private val createPostLikeUsecase: CreatePostLikeUsecase
) {

  @PostMapping
  fun create(@RequestBody command: PostCommand): Long {
    return createPostUsecase.execute(command)
  }

  @GetMapping("/daily-post-counts")
  fun getDailyPostCount(requestDto: DailyPostCountRequestDto): List<DailyPostCountResponseDto> {
    return postReadService.getDailyPostCount(requestDto)
  }

  @GetMapping("/{memberId}")
  fun getPosts(
    @PathVariable memberId: Long,
    pageable: Pageable
  ): Page<PostDto> {
    return postReadService.getPosts(memberId, pageable)
  }

  @GetMapping("/{memberId}/cursor")
  fun getPostsByCursor(
    @PathVariable memberId: Long,
    cursorRequest: CursorRequest
  ): PageCursor<Post> {
    return postReadService.getPosts(memberId, cursorRequest)
  }

  @GetMapping("/members/{memberId}/timelines")
  fun getTimelines(
    @PathVariable memberId: Long,
    cursorRequest: CursorRequest
  ): PageCursor<Post> {
//    return getTimelinePostsUsecase.execute(memberId, cursorRequest)
    return getTimelinePostsUsecase.executeByTimeline(memberId, cursorRequest)
  }

  @PostMapping("/{postId}/like/v1")
  fun likePostV1(@PathVariable postId: Long) {
    postWriteService.likePost(postId)
  }

  @PostMapping("/{postId}/like/v2")
  fun likePostV2(@PathVariable postId: Long, @RequestParam memberId: Long) {
    createPostLikeUsecase.execute(postId, memberId)
  }

  @PostMapping("/{postId}/like/optimistic-lock")
  fun likePostByOptimisticLock(@PathVariable postId: Long) {
    postWriteService.likePostByOptimisticLock(postId)
  }
}
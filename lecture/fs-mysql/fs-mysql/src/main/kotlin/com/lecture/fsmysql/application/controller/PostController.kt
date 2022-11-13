package com.lecture.fsmysql.application.controller

import com.lecture.fsmysql.application.usecase.GetTimelinePostsUsecase
import com.lecture.fsmysql.common.CursorRequest
import com.lecture.fsmysql.common.PageCursor
import com.lecture.fsmysql.domain.post.dto.DailyPostCountRequestDto
import com.lecture.fsmysql.domain.post.dto.DailyPostCountResponseDto
import com.lecture.fsmysql.domain.post.dto.PostCommand
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
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
  private val postWriteService: PostWriteService,
  private val postReadService: PostReadService,
  private val getTimelinePostsUsecase: GetTimelinePostsUsecase
) {

  @PostMapping
  fun create(@RequestBody command: PostCommand): Long {
    return postWriteService.create(command)
  }

  @GetMapping("/daily-post-counts")
  fun getDailyPostCount(requestDto: DailyPostCountRequestDto): List<DailyPostCountResponseDto> {
    return postReadService.getDailyPostCount(requestDto)
  }

  @GetMapping("/{memberId}")
  fun getPosts(
    @PathVariable memberId: Long,
    pageable: Pageable
  ): Page<Post> {
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
    return getTimelinePostsUsecase.exuecte(memberId, cursorRequest)
  }
}
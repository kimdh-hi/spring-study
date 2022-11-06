package com.lecture.fsmysql.application.controler

import com.lecture.fsmysql.domain.post.dto.DailyPostCountRequestDto
import com.lecture.fsmysql.domain.post.dto.DailyPostCountResponseDto
import com.lecture.fsmysql.domain.post.dto.PostCommand
import com.lecture.fsmysql.domain.post.service.PostReadService
import com.lecture.fsmysql.domain.post.service.PostWriteService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
  private val postWriteService: PostWriteService,
  private val postReadService: PostReadService
) {

  @PostMapping
  fun create(@RequestBody command: PostCommand): Long {
    return postWriteService.create(command)
  }

  @GetMapping("/daily-post-counts")
  fun getDailyPostCount(requestDto: DailyPostCountRequestDto): List<DailyPostCountResponseDto> {
    return postReadService.getDailyPostCount(requestDto)
  }
}
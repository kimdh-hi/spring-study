package com.lecture.snsapp.controller

import com.lecture.snsapp.common.Response
import com.lecture.snsapp.service.PostService
import com.lecture.snsapp.vo.PostCreateRequestVO
import com.lecture.snsapp.vo.PostCreateResponseVO
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
  private val postService: PostService
) {
  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping
  fun create(
    @RequestBody requestVO: PostCreateRequestVO,
    authentication: Authentication
  ): Response<PostCreateResponseVO> {
    log.info("authentication: {}", authentication)
    val responseVO = postService.create(requestVO.title, requestVO.body, authentication.name)
    return Response.success(result = responseVO)
  }
}
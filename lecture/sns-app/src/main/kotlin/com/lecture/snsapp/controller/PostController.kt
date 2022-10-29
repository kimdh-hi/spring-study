package com.lecture.snsapp.controller

import com.lecture.snsapp.common.Response
import com.lecture.snsapp.service.PostService
import com.lecture.snsapp.vo.PostCreateRequestVO
import com.lecture.snsapp.vo.PostCreateResponseVO
import com.lecture.snsapp.vo.PostModifyRequestVO
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
    val user = authentication.principal as User
    val responseVO = postService.create(requestVO.title, requestVO.body, user.username)
    return Response.success(result = responseVO)
  }

  @PutMapping("/{id}")
  fun modify(
    @PathVariable id: String, @RequestBody requestVO: PostModifyRequestVO,
    authentication: Authentication
  ): Response<Unit> {
    val user = authentication.principal as User
    postService.modify(id, requestVO.title, requestVO.body, user.username)
    return Response.success()
  }
}
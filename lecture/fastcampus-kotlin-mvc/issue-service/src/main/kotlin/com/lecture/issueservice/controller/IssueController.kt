package com.lecture.issueservice.controller

import com.lecture.issueservice.config.AuthUser
import com.lecture.issueservice.model.IssueRequest
import com.lecture.issueservice.model.IssueResponse
import com.lecture.issueservice.service.IssueService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/issues")
class IssueController(
  private val issueService: IssueService
) {

  @PostMapping
  fun create(
    authUser: AuthUser,
    @RequestBody request: IssueRequest): ResponseEntity<IssueResponse> {
    val response = issueService.create(authUser.userId, request)
    return ResponseEntity.ok(response)
  }
}
package com.lecture.issueservice.controller

import com.lecture.issueservice.config.AuthUser
import com.lecture.issueservice.domain.enums.IssueStatus
import com.lecture.issueservice.model.IssueRequest
import com.lecture.issueservice.model.IssueResponse
import com.lecture.issueservice.service.IssueService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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

  @GetMapping
  fun getAll(
    authUser: AuthUser,
    @RequestParam(required = false, defaultValue = "TODO") status: IssueStatus,
  ): ResponseEntity<List<IssueResponse>> {
    val response = issueService.getAll(status)
    return ResponseEntity.ok(response)
  }

  @GetMapping("/{issueId}")
  fun get(
    authUser: AuthUser,
    @PathVariable issueId: Long,
  ): ResponseEntity<IssueResponse> {
    val response = issueService.get(issueId)
    return ResponseEntity.ok(response)
  }
}
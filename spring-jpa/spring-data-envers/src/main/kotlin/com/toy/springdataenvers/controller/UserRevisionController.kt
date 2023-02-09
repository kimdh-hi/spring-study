package com.toy.springdataenvers.controller

import com.toy.springdataenvers.service.UserRevisionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users/revision")
class UserRevisionController(
  private val revisionService: UserRevisionService
) {

  @GetMapping("/{userId}/revisions")
  fun findRevisions(@PathVariable userId: String) = revisionService.findRevisions(userId)

  @GetMapping("/{userId}/last-change-revision")
  fun findLastChangeRevision(@PathVariable userId: String) = revisionService.findLastChangeRevision(userId)
}
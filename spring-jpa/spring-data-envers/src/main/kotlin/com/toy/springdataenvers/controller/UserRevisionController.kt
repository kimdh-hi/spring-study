package com.toy.springdataenvers.controller

import com.fasterxml.jackson.annotation.JsonFormat
import com.toy.springdataenvers.service.UserRevisionService
import org.springframework.data.jpa.domain.AbstractAuditable_.createdDate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/users/revision")
class UserRevisionController(
  private val revisionService: UserRevisionService
) {

  @GetMapping("/{userId}/revisions")
  fun findRevisions(@PathVariable userId: String) = revisionService.findRevisions(userId)

  @GetMapping("/{userId}/last-change-revision")
  fun findLastChangeRevision(@PathVariable userId: String) = revisionService.findLastChangeRevision(userId)

  @GetMapping("/{userId}/revisions-by")
  fun findRevisionsByCreatedDate(
    @PathVariable userId: String,
    @ModelAttribute vo: UserRevisionSearchVO
  ) = revisionService.findRevisionsByIdAndCreatedDate(userId, vo.createdDate)
}

data class UserRevisionSearchVO(
  @field:DateTimeFormat(pattern = "yyyy-MM-dd:HH:mm:ss")
  val createdDate: LocalDateTime
)
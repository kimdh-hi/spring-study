package com.lecture.issueservice.service

import com.lecture.issueservice.domain.Issue
import com.lecture.issueservice.model.IssueRequest
import com.lecture.issueservice.model.IssueResponse
import com.lecture.issueservice.repository.IssueRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class IssueService(
  private val issueRepository: IssueRepository
) {

  @Transactional
  fun create(userId: Long, request: IssueRequest): IssueResponse {
    val issue = Issue(
      summary = request.summary,
      description = request.description,
      userId = userId,
      type = request.type,
      priority = request.priority,
      status = request.status
    )
    return IssueResponse(issueRepository.save(issue))
  }
}
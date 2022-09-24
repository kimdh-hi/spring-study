package com.lecture.issueservice.repository

import com.lecture.issueservice.domain.Issue
import com.lecture.issueservice.domain.enums.IssueStatus
import org.springframework.data.jpa.repository.JpaRepository

interface IssueRepository: JpaRepository<Issue, Long> {
  fun findAllByStatusOrderByCreatedAtDesc(status: IssueStatus): List<Issue>
}
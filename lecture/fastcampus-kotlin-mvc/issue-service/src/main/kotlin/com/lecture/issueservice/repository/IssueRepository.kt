package com.lecture.issueservice.repository

import com.lecture.issueservice.domain.Issue
import org.springframework.data.jpa.repository.JpaRepository

interface IssueRepository: JpaRepository<Issue, Long> {
}
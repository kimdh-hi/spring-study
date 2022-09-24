package com.lecture.issueservice.domain.enums

enum class IssueStatus {
  TODO, IN_PROGRESS, RESOLVED;

  companion object {
    operator fun invoke(status: String) = IssueStatus.valueOf(status.uppercase())
  }
}

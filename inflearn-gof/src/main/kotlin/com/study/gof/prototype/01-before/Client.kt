package com.study.gof.prototype.`01-before`

fun main() {
  val githubRepository = GithubRepository(user = "user", name = "name")

  val githubIssue = GithubIssue(id = "issue-01", title = "title", githubRepository = githubRepository)

  githubIssue
}
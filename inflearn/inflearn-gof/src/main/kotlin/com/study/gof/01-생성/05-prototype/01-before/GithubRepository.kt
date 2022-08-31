package com.study.gof.`05-prototype`.`01-before`

class GithubRepository(
  var user: String,
  var name: String
)

class GithubIssue (
  var id: String,
  var title: String,
  var githubRepository: GithubRepository
)
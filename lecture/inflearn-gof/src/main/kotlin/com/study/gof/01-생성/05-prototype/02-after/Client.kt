package com.study.gof.`05-prototype`.`02-after`

fun main() {
  val githubRepository = GithubRepository(user = "user", name = "name")

  val githubIssue = GithubIssue(id = "issue-01", title = "title", githubRepository = githubRepository)

  // 두번째 github issue를 생성하려면 ? => clone ...

// 깊은복사
  val clone = githubIssue.clone()
  println(githubIssue.githubRepository !== clone.githubRepository)

// 얕은복사
//  val githubIssue2 = githubIssue.clone()
//  println("서로 다른 인스턴스인가? : ${githubIssue !== githubIssue2}")
//  println("eqauls 비교: ${githubIssue == githubIssue2}")
//  println("클래스 타입 비교: ${githubIssue.javaClass == githubIssue2.javaClass}" )
//  println("얕은복사 확인: ${githubIssue.githubRepository === githubIssue2.githubRepository}")
//  githubRepository.name = "newName"
//  println(githubIssue.getUrl())
//  println(githubIssue2.getUrl())

}
package com.study.hexagonal.board.application.domain

data class Author(
  val id: Long? = null,
  val name: String,
  val email: String
) {
  init {
    require(name.isNotBlank()) { "작성자 이름은 필수입니다" }
    require(EMAIL.matches(email)) { "이메일 형식이 올바르지 않습니다: $email" }
  }

  companion object {
    private val EMAIL = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
  }
}

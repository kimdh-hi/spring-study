package com.study.hexagonal.board.adapter.inbound.web

import com.study.hexagonal.board.application.port.inbound.AuthorQuery
import com.study.hexagonal.board.application.port.inbound.AuthorUseCase
import com.study.hexagonal.board.application.domain.Author
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/authors")
class AuthorController(
  private val authorUseCase: AuthorUseCase,
  private val authorQuery: AuthorQuery
) {

  @PostMapping
  fun register(@RequestBody request: RegisterRequest): AuthorResponse =
    AuthorResponse.from(authorUseCase.register(request.name, request.email))

  @GetMapping("/{authorId}")
  fun find(@PathVariable authorId: Long): AuthorResponse =
    AuthorResponse.from(authorQuery.find(authorId))

  data class RegisterRequest(val name: String, val email: String)

  data class AuthorResponse(val id: Long, val name: String, val email: String) {
    companion object {
      fun from(author: Author) = AuthorResponse(author.id!!, author.name, author.email)
    }
  }
}

package com.study.hexagonal.board.application

import com.study.hexagonal.board.application.port.inbound.AuthorQuery
import com.study.hexagonal.board.application.port.inbound.AuthorUseCase
import com.study.hexagonal.board.application.port.outbound.AuthorRepository
import com.study.hexagonal.board.application.domain.Author
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthorService(
  private val authorRepository: AuthorRepository
) : AuthorUseCase, AuthorQuery {

  override fun register(name: String, email: String): Author {
    require(!authorRepository.existsByEmail(email)) { "이미 가입된 이메일입니다: $email" }
    return authorRepository.save(Author(name = name, email = email))
  }

  @Transactional(readOnly = true)
  override fun find(authorId: Long): Author =
    authorRepository.findById(authorId) ?: throw NoSuchElementException("작성자를 찾을 수 없습니다: $authorId")
}

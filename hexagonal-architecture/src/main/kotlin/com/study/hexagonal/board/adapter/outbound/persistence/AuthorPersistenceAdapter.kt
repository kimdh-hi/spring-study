package com.study.hexagonal.board.adapter.outbound.persistence

import com.study.hexagonal.board.application.port.outbound.AuthorRepository
import com.study.hexagonal.board.application.domain.Author
import org.springframework.stereotype.Repository

@Repository
class AuthorPersistenceAdapter(
  private val jpaRepository: AuthorJpaRepository
) : AuthorRepository {

  override fun save(author: Author): Author =
    jpaRepository.save(AuthorJpaEntity(author.id, author.name, author.email)).toDomain()

  override fun findById(authorId: Long): Author? =
    jpaRepository.findById(authorId).map { it.toDomain() }.orElse(null)

  override fun existsById(authorId: Long): Boolean = jpaRepository.existsById(authorId)

  override fun existsByEmail(email: String): Boolean = jpaRepository.existsByEmail(email)
}

private fun AuthorJpaEntity.toDomain() = Author(id, name, email)

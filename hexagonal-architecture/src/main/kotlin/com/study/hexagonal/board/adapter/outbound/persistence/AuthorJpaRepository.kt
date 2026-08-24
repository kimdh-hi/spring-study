package com.study.hexagonal.board.adapter.outbound.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface AuthorJpaRepository : JpaRepository<AuthorJpaEntity, Long> {
  fun existsByEmail(email: String): Boolean
}

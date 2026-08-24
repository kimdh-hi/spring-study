package com.study.hexagonal.board.application.port.outbound

import com.study.hexagonal.board.application.domain.Author

interface AuthorRepository {
  fun save(author: Author): Author
  fun findById(authorId: Long): Author?
  fun existsById(authorId: Long): Boolean
  fun existsByEmail(email: String): Boolean
}

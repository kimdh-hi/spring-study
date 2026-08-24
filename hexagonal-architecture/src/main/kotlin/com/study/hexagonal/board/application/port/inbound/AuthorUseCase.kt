package com.study.hexagonal.board.application.port.inbound

import com.study.hexagonal.board.application.domain.Author

interface AuthorUseCase {
  fun register(name: String, email: String): Author
}

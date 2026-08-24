package com.study.hexagonal.board.application.port.inbound

import com.study.hexagonal.board.application.domain.Author

interface AuthorQuery {
  fun find(authorId: Long): Author
}

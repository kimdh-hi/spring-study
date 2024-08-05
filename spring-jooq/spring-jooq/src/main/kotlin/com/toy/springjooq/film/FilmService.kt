package com.toy.springjooq.film

import org.springframework.stereotype.Service

@Service
class FilmService(
  private val filmRepository: FilmRepository
) {

  fun getList(page: Long, offset: Long): List<FilmWithActor> {
    return filmRepository.findAllWithActors(page, offset)
  }
}

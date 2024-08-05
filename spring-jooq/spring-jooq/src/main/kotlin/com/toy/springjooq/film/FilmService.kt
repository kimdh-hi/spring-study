package com.toy.springjooq.film

import org.springframework.stereotype.Service

@Service
class FilmService(
  private val filmRepository: FilmRepository
) {

  fun getList(page: Long, pageSize: Long): FilmWithActorPagedResponse {
    val fileWithActors = filmRepository.findAllWithActors(page, pageSize)
      .map { FilmWithActorPagedResponse.FilmActorResponse(it) }
    return FilmWithActorPagedResponse(PagedResponse(page, pageSize), fileWithActors)
  }
}

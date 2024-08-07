package com.toy.springjooq.film

import org.jooq.Configuration
import org.jooq.generated.tables.daos.FilmDao
import org.jooq.generated.tables.pojos.Film
import org.springframework.stereotype.Repository

@Repository
class FilmRepositoryComposition(
  private val configuration: Configuration
) {

  private val dao = FilmDao(configuration)

  fun findById(id: Long): Film? {
    return dao.findById(id)
  }
}




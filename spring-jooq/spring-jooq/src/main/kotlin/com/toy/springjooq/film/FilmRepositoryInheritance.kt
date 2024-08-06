package com.toy.springjooq.film

import org.jooq.Configuration
import org.jooq.generated.tables.daos.FilmDao
import org.springframework.stereotype.Repository

@Repository
class FilmRepositoryInheritance(
  private val configuration: Configuration
) : FilmDao(configuration) {

}




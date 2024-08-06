package com.toy.springjooq.film

import com.toy.springjooq.film.vo.FileInfo
import com.toy.springjooq.film.vo.FilmWithActor
import org.jooq.DSLContext
import org.jooq.generated.tables.JActor
import org.jooq.generated.tables.JFilm
import org.jooq.generated.tables.JFilmActor
import org.jooq.generated.tables.pojos.Film
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class FilmRepository(
  private val dslContext: DSLContext
) {

  private val FILM = JFilm.FILM
  private val FILM_ACTOR = JFilmActor.FILM_ACTOR
  private val ACTOR = JActor.ACTOR

  fun findById(id: Long): Film? {
    return dslContext.select()
      .from(FILM)
      .where(FILM.FILM_ID.eq(id))
      .fetchOneInto(Film::class.java)
  }

  fun findByIdV2(id: Long): FileInfo? {
    return dslContext.select(
      FILM.FILM_ID,
      FILM.TITLE,
      FILM.DESCRIPTION
    )
      .from(FILM)
      .where(FILM.FILM_ID.eq(id))
      .fetchOneInto(FileInfo::class.java)
  }

  fun findAllWithActors(page: Long, offset: Long): List<FilmWithActor> {
    return dslContext.select(
      DSL.row(FILM.fields()),
      DSL.row(FILM_ACTOR.fields()),
      DSL.row(ACTOR.fields()),
    )
      .from(FILM)
      .join(FILM_ACTOR).on(FILM_ACTOR.FILM_ID.eq(FILM.FILM_ID))
      .join(ACTOR).on(ACTOR.ACTOR_ID.eq(FILM_ACTOR.ACTOR_ID))
      .limit(10)
      .fetchInto(FilmWithActor::class.java)
  }
}




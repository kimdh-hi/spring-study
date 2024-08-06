package com.toy.springjooq.film.vo

import org.jooq.generated.tables.pojos.Actor
import org.jooq.generated.tables.pojos.Film
import org.jooq.generated.tables.pojos.FilmActor

data class FileInfo(
  val id: Long,
  val title: String,
  val description: String
)

data class FilmWithActor(
  val file: Film,
  val fileActor: FilmActor,
  val actor: Actor,
)

data class PagedResponse(
  val page: Long,
  val pageSize: Long,
)

data class FilmWithActorPagedResponse(
  val page: PagedResponse,
  val filmWithActors: List<FilmActorResponse>
) {
  data class FilmActorResponse(
    val fileTitle: String?,
    val actorFullName: String,
    val filmId: Long?,
  ) {
    constructor(fileWithActor: FilmWithActor): this(
      fileTitle = fileWithActor.file.title,
      actorFullName = "${fileWithActor.actor.firstName} ${fileWithActor.actor.lastName}",
      filmId = fileWithActor.file.filmId
    )
  }
}

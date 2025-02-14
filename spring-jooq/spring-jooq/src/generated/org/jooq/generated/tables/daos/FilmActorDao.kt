/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.daos


import java.time.LocalDateTime

import kotlin.collections.List

import org.jooq.Configuration
import org.jooq.Record2
import org.jooq.generated.tables.JFilmActor
import org.jooq.generated.tables.pojos.FilmActor
import org.jooq.generated.tables.records.FilmActorRecord
import org.jooq.impl.AutoConverter
import org.jooq.impl.DAOImpl
import org.jooq.types.UInteger


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class FilmActorDao(configuration: Configuration?) : DAOImpl<FilmActorRecord, FilmActor, Record2<Long?, Long?>>(JFilmActor.FILM_ACTOR, FilmActor::class.java, configuration) {

    /**
     * Create a new FilmActorDao without any configuration
     */
    constructor(): this(null)

    override fun getId(o: FilmActor): Record2<Long?, Long?> = compositeKeyRecord(o.actorId, o.filmId)

    /**
     * Fetch records that have <code>actor_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfJActorId(lowerInclusive: Long?, upperInclusive: Long?): List<FilmActor> = fetchRange(JFilmActor.FILM_ACTOR.ACTOR_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>actor_id IN (values)</code>
     */
    fun fetchByJActorId(vararg values: Long): List<FilmActor> = fetch(JFilmActor.FILM_ACTOR.ACTOR_ID, *values.toTypedArray())

    /**
     * Fetch records that have <code>film_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfJFilmId(lowerInclusive: Long?, upperInclusive: Long?): List<FilmActor> = fetchRange(JFilmActor.FILM_ACTOR.FILM_ID, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>film_id IN (values)</code>
     */
    fun fetchByJFilmId(vararg values: Long): List<FilmActor> = fetch(JFilmActor.FILM_ACTOR.FILM_ID, *values.toTypedArray())

    /**
     * Fetch records that have <code>last_update BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    fun fetchRangeOfJLastUpdate(lowerInclusive: LocalDateTime?, upperInclusive: LocalDateTime?): List<FilmActor> = fetchRange(JFilmActor.FILM_ACTOR.LAST_UPDATE, lowerInclusive, upperInclusive)

    /**
     * Fetch records that have <code>last_update IN (values)</code>
     */
    fun fetchByJLastUpdate(vararg values: LocalDateTime): List<FilmActor> = fetch(JFilmActor.FILM_ACTOR.LAST_UPDATE, *values)
}

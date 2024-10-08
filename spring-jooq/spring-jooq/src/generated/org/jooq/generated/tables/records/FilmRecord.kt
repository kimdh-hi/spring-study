/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records


import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Year

import org.jooq.Record1
import org.jooq.generated.enums.FilmRating
import org.jooq.generated.tables.JFilm
import org.jooq.generated.tables.pojos.Film
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class FilmRecord() : UpdatableRecordImpl<FilmRecord>(JFilm.FILM) {

    open var filmId: Long?
        set(value): Unit = set(0, value)
        get(): Long? = get(0) as Long?

    open var title: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    open var description: String?
        set(value): Unit = set(2, value)
        get(): String? = get(2) as String?

    open var releaseYear: Year?
        set(value): Unit = set(3, value)
        get(): Year? = get(3) as Year?

    open var languageId: Long?
        set(value): Unit = set(4, value)
        get(): Long? = get(4) as Long?

    open var originalLanguageId: Long?
        set(value): Unit = set(5, value)
        get(): Long? = get(5) as Long?

    open var rentalDuration: Int?
        set(value): Unit = set(6, value)
        get(): Int? = get(6) as Int?

    open var rentalRate: BigDecimal?
        set(value): Unit = set(7, value)
        get(): BigDecimal? = get(7) as BigDecimal?

    open var length: Int?
        set(value): Unit = set(8, value)
        get(): Int? = get(8) as Int?

    open var replacementCost: BigDecimal?
        set(value): Unit = set(9, value)
        get(): BigDecimal? = get(9) as BigDecimal?

    open var rating: FilmRating?
        set(value): Unit = set(10, value)
        get(): FilmRating? = get(10) as FilmRating?

    open var specialFeatures: String?
        set(value): Unit = set(11, value)
        get(): String? = get(11) as String?

    open var lastUpdate: LocalDateTime?
        set(value): Unit = set(12, value)
        get(): LocalDateTime? = get(12) as LocalDateTime?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    /**
     * Create a detached, initialised FilmRecord
     */
    constructor(filmId: Long? = null, title: String? = null, description: String? = null, releaseYear: Year? = null, languageId: Long? = null, originalLanguageId: Long? = null, rentalDuration: Int? = null, rentalRate: BigDecimal? = null, length: Int? = null, replacementCost: BigDecimal? = null, rating: FilmRating? = null, specialFeatures: String? = null, lastUpdate: LocalDateTime? = null): this() {
        this.filmId = filmId
        this.title = title
        this.description = description
        this.releaseYear = releaseYear
        this.languageId = languageId
        this.originalLanguageId = originalLanguageId
        this.rentalDuration = rentalDuration
        this.rentalRate = rentalRate
        this.length = length
        this.replacementCost = replacementCost
        this.rating = rating
        this.specialFeatures = specialFeatures
        this.lastUpdate = lastUpdate
        resetChangedOnNotNull()
    }

    /**
     * Create a detached, initialised FilmRecord
     */
    constructor(value: Film?): this() {
        if (value != null) {
            this.filmId = value.filmId
            this.title = value.title
            this.description = value.description
            this.releaseYear = value.releaseYear
            this.languageId = value.languageId
            this.originalLanguageId = value.originalLanguageId
            this.rentalDuration = value.rentalDuration
            this.rentalRate = value.rentalRate
            this.length = value.length
            this.replacementCost = value.replacementCost
            this.rating = value.rating
            this.specialFeatures = value.specialFeatures
            this.lastUpdate = value.lastUpdate
            resetChangedOnNotNull()
        }
    }
}

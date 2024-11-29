/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables


import java.time.LocalDateTime

import kotlin.collections.Collection
import kotlin.collections.List

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.InverseForeignKey
import org.jooq.Name
import org.jooq.Path
import org.jooq.PlainSQL
import org.jooq.QueryPart
import org.jooq.Record
import org.jooq.SQL
import org.jooq.Schema
import org.jooq.Select
import org.jooq.Stringly
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.generated.JSakila
import org.jooq.generated.keys.FK_FILM_CATEGORY_CATEGORY
import org.jooq.generated.keys.FK_FILM_CATEGORY_FILM
import org.jooq.generated.keys.KEY_FILM_CATEGORY_PRIMARY
import org.jooq.generated.tables.JCategory.CategoryPath
import org.jooq.generated.tables.JFilm.FilmPath
import org.jooq.generated.tables.records.FilmCategoryRecord
import org.jooq.impl.AutoConverter
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl
import org.jooq.types.UInteger


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class JFilmCategory(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, FilmCategoryRecord>?,
    parentPath: InverseForeignKey<out Record, FilmCategoryRecord>?,
    aliased: Table<FilmCategoryRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<FilmCategoryRecord>(
    alias,
    JSakila.SAKILA,
    path,
    childPath,
    parentPath,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table(),
    where,
) {
    companion object {

        /**
         * The reference instance of <code>sakila.film_category</code>
         */
        val FILM_CATEGORY: JFilmCategory = JFilmCategory()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<FilmCategoryRecord> = FilmCategoryRecord::class.java

    /**
     * The column <code>sakila.film_category.film_id</code>.
     */
    val FILM_ID: TableField<FilmCategoryRecord, Long?> = createField(DSL.name("film_id"), SQLDataType.INTEGERUNSIGNED.nullable(false), this, "", AutoConverter<UInteger, Long>(UInteger::class.java, Long::class.java))

    /**
     * The column <code>sakila.film_category.category_id</code>.
     */
    val CATEGORY_ID: TableField<FilmCategoryRecord, Long?> = createField(DSL.name("category_id"), SQLDataType.INTEGERUNSIGNED.nullable(false), this, "", AutoConverter<UInteger, Long>(UInteger::class.java, Long::class.java))

    /**
     * The column <code>sakila.film_category.last_update</code>.
     */
    val LAST_UPDATE: TableField<FilmCategoryRecord, LocalDateTime?> = createField(DSL.name("last_update"), SQLDataType.LOCALDATETIME(0).nullable(false).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.LOCALDATETIME)), this, "")

    private constructor(alias: Name, aliased: Table<FilmCategoryRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<FilmCategoryRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<FilmCategoryRecord>?, where: Condition): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>sakila.film_category</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>sakila.film_category</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>sakila.film_category</code> table reference
     */
    constructor(): this(DSL.name("film_category"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, FilmCategoryRecord>?, parentPath: InverseForeignKey<out Record, FilmCategoryRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, FILM_CATEGORY, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class FilmCategoryPath : JFilmCategory, Path<FilmCategoryRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, FilmCategoryRecord>?, parentPath: InverseForeignKey<out Record, FilmCategoryRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<FilmCategoryRecord>): super(alias, aliased)
        override fun `as`(alias: String): FilmCategoryPath = FilmCategoryPath(DSL.name(alias), this)
        override fun `as`(alias: Name): FilmCategoryPath = FilmCategoryPath(alias, this)
        override fun `as`(alias: Table<*>): FilmCategoryPath = FilmCategoryPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else JSakila.SAKILA
    override fun getPrimaryKey(): UniqueKey<FilmCategoryRecord> = KEY_FILM_CATEGORY_PRIMARY
    override fun getReferences(): List<ForeignKey<FilmCategoryRecord, *>> = listOf(FK_FILM_CATEGORY_FILM, FK_FILM_CATEGORY_CATEGORY)

    private lateinit var _film: FilmPath

    /**
     * Get the implicit join path to the <code>sakila.film</code> table.
     */
    fun film(): FilmPath {
        if (!this::_film.isInitialized)
            _film = FilmPath(this, FK_FILM_CATEGORY_FILM, null)

        return _film;
    }

    val film: FilmPath
        get(): FilmPath = film()

    private lateinit var _category: CategoryPath

    /**
     * Get the implicit join path to the <code>sakila.category</code> table.
     */
    fun category(): CategoryPath {
        if (!this::_category.isInitialized)
            _category = CategoryPath(this, FK_FILM_CATEGORY_CATEGORY, null)

        return _category;
    }

    val category: CategoryPath
        get(): CategoryPath = category()
    override fun `as`(alias: String): JFilmCategory = JFilmCategory(DSL.name(alias), this)
    override fun `as`(alias: Name): JFilmCategory = JFilmCategory(alias, this)
    override fun `as`(alias: Table<*>): JFilmCategory = JFilmCategory(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): JFilmCategory = JFilmCategory(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): JFilmCategory = JFilmCategory(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): JFilmCategory = JFilmCategory(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition): JFilmCategory = JFilmCategory(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): JFilmCategory = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition): JFilmCategory = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>): JFilmCategory = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): JFilmCategory = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): JFilmCategory = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): JFilmCategory = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): JFilmCategory = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): JFilmCategory = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): JFilmCategory = where(DSL.notExists(select))
}
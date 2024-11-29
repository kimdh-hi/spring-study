/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.routines


import org.jooq.Parameter
import org.jooq.generated.JSakila
import org.jooq.impl.AbstractRoutine
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class JFilmNotInStock : AbstractRoutine<java.lang.Void>("film_not_in_stock", JSakila.SAKILA) {
    companion object {

        /**
         * The parameter <code>sakila.film_not_in_stock.p_film_id</code>.
         */
        val P_FILM_ID: Parameter<Int?> = Internal.createParameter("p_film_id", SQLDataType.INTEGER, false, false)

        /**
         * The parameter <code>sakila.film_not_in_stock.p_store_id</code>.
         */
        val P_STORE_ID: Parameter<Int?> = Internal.createParameter("p_store_id", SQLDataType.INTEGER, false, false)

        /**
         * The parameter <code>sakila.film_not_in_stock.p_film_count</code>.
         */
        val P_FILM_COUNT: Parameter<Int?> = Internal.createParameter("p_film_count", SQLDataType.INTEGER, false, false)
    }

    init {
        addInParameter(P_FILM_ID)
        addInParameter(P_STORE_ID)
        addOutParameter(P_FILM_COUNT)
    }

    /**
     * Set the <code>p_film_id</code> parameter IN value to the routine
     */
    fun setPFilmId(value: Int?): Unit = setValue(P_FILM_ID, value)

    /**
     * Set the <code>p_store_id</code> parameter IN value to the routine
     */
    fun setPStoreId(value: Int?): Unit = setValue(P_STORE_ID, value)

    /**
     * Get the <code>p_film_count</code> parameter OUT value from the routine
     */
    fun getPFilmCount(): Int? = get(P_FILM_COUNT)
}
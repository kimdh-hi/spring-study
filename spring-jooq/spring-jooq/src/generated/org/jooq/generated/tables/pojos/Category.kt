/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.pojos


import java.io.Serializable
import java.time.LocalDateTime


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
data class Category(
    var categoryId: Long? = null,
    var name: String? = null,
    var lastUpdate: LocalDateTime? = null
): Serializable {


    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        if (this::class != other::class)
            return false
        val o: Category = other as Category
        if (this.categoryId == null) {
            if (o.categoryId != null)
                return false
        }
        else if (this.categoryId != o.categoryId)
            return false
        if (this.name == null) {
            if (o.name != null)
                return false
        }
        else if (this.name != o.name)
            return false
        if (this.lastUpdate == null) {
            if (o.lastUpdate != null)
                return false
        }
        else if (this.lastUpdate != o.lastUpdate)
            return false
        return true
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (if (this.categoryId == null) 0 else this.categoryId.hashCode())
        result = prime * result + (if (this.name == null) 0 else this.name.hashCode())
        result = prime * result + (if (this.lastUpdate == null) 0 else this.lastUpdate.hashCode())
        return result
    }

    override fun toString(): String {
        val sb = StringBuilder("Category (")

        sb.append(categoryId)
        sb.append(", ").append(name)
        sb.append(", ").append(lastUpdate)

        sb.append(")")
        return sb.toString()
    }
}

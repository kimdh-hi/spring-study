package com.toy.springkotlin.entity

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import jakarta.persistence.Transient
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable
import java.util.Objects
import java.util.UUID

@MappedSuperclass
abstract class UuidPrimaryKeyEntity(
  @Id
  @Column(length = 50)
  private val id: String = UUID.randomUUID().toString(),
) : Persistable<String> {

  @Transient
  private var new: Boolean = true

  override fun isNew(): Boolean = new

  @PostPersist
  @PostLoad
  fun load() {
    new = false
  }

  override fun equals(other: Any?): Boolean {
    if (other == null) return false
    if (other !is HibernateProxy && this::class != other::class) return false
    val parseId = when (other) {
      is HibernateProxy -> other.hibernateLazyInitializer.identifier as String
      is UuidPrimaryKeyEntity -> other.id
      else -> return false
    }

    return id == parseId
  }

  override fun hashCode() = Objects.hashCode(id)

  override fun getId(): String = id
}

package com.toy.springkotlin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable
import java.util.*

@JvmInline
value class GroupId(val value: String)

@Entity
@Table(name = "groups")
class Group private constructor(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: GroupId,

  @Column(length = 100, nullable = false)
  var name: String
) : BaseCreateTimestampWithoutIdEntity(), Persistable<GroupId> {

  companion object {
    operator fun invoke(name: String): Group {
      return Group(id = GroupId(""), name = name)
    }
  }

  override fun toString(): String {
    return "Group(groupId=$id, name='$name')"
  }

  override fun equals(other: Any?): Boolean {
    if (other == null) return false
    if (other !is HibernateProxy && this::class != other::class) return false
    val parseId = when (other) {
      is HibernateProxy -> other.hibernateLazyInitializer.identifier as String
      is GroupId -> other.value
      else -> return false
    }

    return id.value == parseId
  }

  override fun hashCode() = Objects.hashCode(id)

  override fun getId(): GroupId? = id

  override fun isNew(): Boolean = createdAt == null
}

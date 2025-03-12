package com.toy.springkotlin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.io.Serializable

@JvmInline
value class GroupId(val value: String) : Serializable

@Entity
@Table(name = "groups")
class Group private constructor(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var groupId: GroupId? = null,

  @Column(length = 100, nullable = false)
  var name: String
) {

  companion object {
    operator fun invoke(name: String): Group {
      return Group(name = name)
    }
  }

  override fun toString(): String {
    return "Group(groupId=$groupId, name='$name')"
  }
}

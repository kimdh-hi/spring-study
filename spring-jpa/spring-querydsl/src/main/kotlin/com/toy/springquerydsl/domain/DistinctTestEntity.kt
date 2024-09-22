package com.toy.springquerydsl.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class DistinctTestDefaultEntity (
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
  var name: String
) {
  override fun toString(): String {
    return "DistinctTestDefaultEntity(id=$id, name='$name')"
  }
}

@Entity
class DistinctTestNameHashCodeEntity (
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
  var name: String
) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as DistinctTestNameHashCodeEntity

    if (name != other.name) return false

    return true
  }

  override fun hashCode(): Int {
    return name.hashCode()
  }

  override fun toString(): String {
    return "DistinctTestNameHashCodeEntity(id=$id, name='$name')"
  }
}


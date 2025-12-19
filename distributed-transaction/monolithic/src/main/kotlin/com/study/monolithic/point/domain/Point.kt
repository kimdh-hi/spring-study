package com.study.monolithic.point.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "points")
class Point private constructor(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  val userId: Long,

  var amount: Long,
) {

  fun use(amount: Long) {
    if (this.amount > amount) {
      throw RuntimeException("don't have enough amount of point")
    }

    this.amount -= amount
  }

  companion object {
    fun of(userId: Long, amount: Long) = Point(
      userId = userId,
      amount = amount
    )
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Point

    return id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 0
  }


}

package com.study.monolithic.order.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "orders")
class Order private constructor(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  var status: OrderStatus = OrderStatus.CREATED
) {

  companion object {
    fun of() = Order()
  }

  fun complete() {
    this.status = OrderStatus.COMPLETED
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Order

    return id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 0
  }

  enum class OrderStatus {
    CREATED, COMPLETED;
  }
}

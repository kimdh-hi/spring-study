package com.lecture.orderservice.domain

import org.hibernate.annotations.GenericGenerator
import java.io.Serial
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "orders")
class Order(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  val productId: String,

  val quantity: Int,

  val unitPrice: Int,

  val totalPrice: Int,

  val userId: String,
): AbstractBaseEntity() {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 8600497529407087863L
  }
}
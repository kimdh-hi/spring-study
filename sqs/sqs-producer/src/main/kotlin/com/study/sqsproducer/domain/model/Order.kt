package com.study.sqsproducer.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "orders")
class Order(
  val productName: String,
  val quantity: Int,
  val customerId: String,
  val createdAt: Instant = Instant.now(),
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,
)

package com.toy.hibernate6.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "tb_random_based_uuid")
class RandomBasedUUID(
  @Id
  @UuidGenerator(style = UuidGenerator.Style.RANDOM)
  @GeneratedValue
  var id: String? = null,

  val data: String
) {
  override fun toString(): String {
    return "RandomBasedUUID(id=$id, data='$data')"
  }
}
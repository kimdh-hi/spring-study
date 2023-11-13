package com.toy.hibernate6.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "tb_time_based_uuid")
class TimeBasedUUID(
  @Id
  @UuidGenerator(style = UuidGenerator.Style.TIME)
  @GeneratedValue
  var id: String? = null,

  val data: String
) {
  override fun toString(): String {
    return "TimeBasedUUID(id=$id, data='$data')"
  }
}
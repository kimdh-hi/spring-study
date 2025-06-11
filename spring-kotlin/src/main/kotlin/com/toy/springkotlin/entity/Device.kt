package com.toy.springkotlin.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.UuidGenerator

@Entity
class Device private constructor(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String? = null,

  @Column(length = 100, nullable = false)
  var deviceKey: String,
) {
  companion object {
    fun of(deviceKey: String) = Device(
      deviceKey = deviceKey,
    )
  }
}

@JvmInline
value class DeviceKey(val value: String)



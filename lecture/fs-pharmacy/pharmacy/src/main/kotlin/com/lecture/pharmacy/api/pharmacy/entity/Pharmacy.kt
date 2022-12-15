package com.lecture.pharmacy.api.pharmacy.entity

import com.lecture.pharmacy.api.common.entity.BaseEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "pharmacy")
class Pharmacy(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = -1L,

  var name: String,
  var address: String,
  var latitude: Double,
  var longitude: Double
): BaseEntity() {

  fun changeAddress(address: String) {
    this.address = address
  }
}
package com.toy.evenetmanagementapi.domain

import java.util.*
import javax.persistence.*

@Entity
class Venue(
  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,
  var name: String,
  var streetAddress: String,
  var streetAddress2: String,
  var city: String,
  var state: String,
  var country: String,
  var postalCode: String,
) : AbstractEntity() {

  fun getResourceId() = id
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Venue

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 0
  }

  override fun toString(): String {
    return "Venue(id=$id, name='$name', streetAddress='$streetAddress', streetAddress2='$streetAddress2', city='$city', state='$state', country='$country', postalCode='$postalCode')"
  }
}
package com.toy.evenetmanagementapi.domain

import java.util.*
import javax.persistence.*

@Entity
class Organizer(
  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,
  var name: String,
) : AbstractEntity() {

  @OneToMany(mappedBy = "organizer")
  var events: Set<Event>? = null

  override fun hashCode(): Int {
    return Objects.hash(id)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Organizer

    if (name != other.name) return false
    if (events != other.events) return false

    return true
  }

  fun getResourceId() = id
}
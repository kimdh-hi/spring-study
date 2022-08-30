package com.toy.evenetmanagementapi.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.rest.core.annotation.RestResource
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
class Event(
  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,
  var name: String,
  var description: String,
  var startTime: ZonedDateTime? = null,
  var endTime: ZonedDateTime? = null,
  var zoneId: ZoneId? = null,
  var started: Boolean? = null,
) : AbstractEntity() {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="organizer_id", nullable = false)
  var organizer: Organizer? = null

  @JsonIgnore
  @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
  var participants: Set<Participant>? = null

  @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
  @RestResource(exported = false)
  var venue: Venue? = null

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Event

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 0
  }

}
package com.toy.evenetmanagementapi.domain

import org.springframework.data.jpa.domain.AbstractPersistable_
import javax.persistence.*

@Entity
class Participant(
  @Id
  @Column(nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @Column(nullable = false)
  var name: String,

  @Column(nullable = false)
  var email: String,

  var checkedIn: Boolean
) : AbstractEntity() {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "event_id", nullable = false, updatable = false)
  var event: Event? = null

  fun getResourceId() = AbstractPersistable_.id


}
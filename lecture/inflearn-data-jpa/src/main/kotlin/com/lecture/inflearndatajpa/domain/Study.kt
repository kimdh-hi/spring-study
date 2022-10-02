package com.lecture.inflearndatajpa.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Study(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var name: String,

  @ManyToOne
  var owner: Account? = null
) {
  fun addOwner(owner: Account) {
    this.owner = owner
    owner.studies.add(this)
  }

  fun removeOwner(owner: Account) {
    this.owner = null
    owner.studies.remove(this)
  }
}
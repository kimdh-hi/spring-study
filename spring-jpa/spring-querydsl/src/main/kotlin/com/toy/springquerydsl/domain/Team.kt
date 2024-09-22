package com.toy.springquerydsl.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Team (
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var name: String,

  @OneToMany(mappedBy = "team")
  var members: MutableList<Member> = mutableListOf()
) {

  override fun toString(): String {
    return "Team(id=$id, name='$name')"
  }
}

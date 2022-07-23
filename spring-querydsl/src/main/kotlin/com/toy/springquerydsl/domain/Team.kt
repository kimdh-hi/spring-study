package com.toy.springquerydsl.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

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
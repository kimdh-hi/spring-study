package com.toy.springquerydsl.domain

import jakarta.persistence.*

@Entity
class Member (
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
  var username: String,
  var age: Int,

  @JoinColumn(name = "team_id")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  var team: Team
) {

  fun changeTeam(team: Team) {
    this.team = team
    team.members.add(this)
  }

  override fun toString(): String {
    return "Member(id=$id, username='$username', age=$age)"
  }

}

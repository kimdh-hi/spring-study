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
  var team: Team,

  @Embedded
  val innerMember: InnerMember? = null,
) {

  fun changeTeam(team: Team, member: Member) {
    this.team = team
    team.members.add(this)
  }

  override fun toString(): String {
    return "Member(id=$id, username='$username', age=$age)"
  }

  fun findInnerMemberUsername() = innerMember?.innerMember?.username
}

@Embeddable
data class InnerMember(
  @OneToOne
  var innerMember: Member? = null
)

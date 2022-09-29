package com.toy.jpabasic.domain

import java.io.Serial
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "tb_group_member")
class GroupMember(
  @ManyToOne(optional = false)
  @JoinColumn(name = "member_id", nullable = false, insertable = false, updatable = false)
  var member: Member,

  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id", nullable = false, insertable = false, updatable = false)
  var group: Group,

  @Column(nullable = false)
  var sbApplicationId: String,
  @Column(nullable = false)
  var sbApiToken: String
) {

  @EmbeddedId
  var id: ID = ID(member.id!!, group.id!!)

  companion object {
    fun of(member: Member, group: Group, sbAppId: String, sbApiToken: String) = GroupMember(
      member = member,
      group = group,
      sbApplicationId = sbAppId,
      sbApiToken = sbApiToken
    )
  }

  @Embeddable
  data class ID (
    @Column(name = "member_id")
    var memberId: String,

    @Column(name = "group_id")
    var groupId: String
  ): Serializable {
    companion object {
      @Serial
      private const val serialVersionUID: Long = 1896183885037009588L
    }
  }

  override fun toString(): String {
    return "GroupMember(id=$id, member.id=${member.id}, group.id=${group.id}, sbApplicationId='$sbApplicationId', sbApiToken='$sbApiToken')"
  }
}
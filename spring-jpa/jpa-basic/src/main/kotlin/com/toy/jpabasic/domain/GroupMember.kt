package com.toy.jpabasic.domain

import java.io.Serial
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "tb_group_member")
class GroupMember(

  @EmbeddedId
  var id: ID,

  @ManyToOne(optional = false)
  @JoinColumn(name = "member_id", nullable = false, insertable = false, updatable = false)
  final var member: Member,

  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id", nullable = false, insertable = false, updatable = false)
  final var group: Group,

  @Column(nullable = false)
  var sbApplicationId: String,

  @Column(nullable = false)
  var sbApiToken: String,

  @OneToOne
  @JoinColumn(name = "group_member_option_id")
  var groupMemberOption: GroupMemberOption? = null
) {
  companion object {
    fun of(member: Member, group: Group, sbAppId: String, sbApiToken: String): GroupMember {
      val groupMember = GroupMember(
        id = ID(),
        member = member,
        group = group,
        sbApplicationId = sbAppId,
        sbApiToken = sbApiToken
      )
      groupMember.id = ID(group.id, member.id)
      return groupMember
    }
  }

  @Embeddable
  data class ID (
    @Column(name = "group_id")
    val groupId: String? = null,

    @Column(name = "member_id")
    var memberId: String? = null
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
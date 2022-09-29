package com.toy.jpabasic.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "tb_group_member")
class GroupMember(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  @ManyToOne
  @JoinColumn(name = "member_id")
  var member: Member,

  @ManyToOne
  @JoinColumn(name = "group_id")
  var group: Group,

  @Column(nullable = false)
  var sbApplicationId: String,
  @Column(nullable = false)
  var sbApiToken: String
) {
  override fun toString(): String {
    return "GroupMember(id=$id, member.id=${member.id}, group.id=${group.id}, sbApplicationId='$sbApplicationId', sbApiToken='$sbApiToken')"
  }
}
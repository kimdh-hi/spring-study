package com.toy.jpabasic.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_group")
class Group(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String,

  @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE])
  val groupMembers: MutableList<GroupMember> = mutableListOf(),

  @OneToOne(mappedBy = "group", cascade = [CascadeType.ALL], orphanRemoval = true)
  var groupOption: GroupOption? = null
) {
  override fun toString(): String {
    return "Group(id=$id, name='$name')"
  }
}
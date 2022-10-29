package com.toy.jpabasic.domain

import org.hibernate.annotations.GenericGenerator
import java.util.stream.Collectors
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "tb_member")
class Member(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String,

  var age: Int? = null,

  @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE])
  val groupMembers: MutableList<GroupMember> = mutableListOf()
) {
  override fun toString(): String {
    return "Member(id=$id, name='$name')"
  }
}
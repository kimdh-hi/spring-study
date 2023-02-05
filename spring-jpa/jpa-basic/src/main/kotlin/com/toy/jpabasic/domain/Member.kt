package com.toy.jpabasic.domain

import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.GenericGenerator
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "tb_member")
@DynamicInsert
class Member(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  @Column(length = 50)
  var name: String,

  var age: Int? = null,

  @ColumnDefault("'hello'")
  var test: String? = null,

  @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE])
  val groupMembers: MutableList<GroupMember> = mutableListOf()
) {
  override fun toString(): String {
    return "Member(id=$id, name='$name')"
  }
}
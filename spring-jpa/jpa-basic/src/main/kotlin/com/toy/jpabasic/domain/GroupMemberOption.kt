package com.toy.jpabasic.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tb_group_member_option")
class GroupMemberOption(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  @Column(name = "opt1")
  var opt1: String
) {
  override fun toString(): String {
    return "GroupMemberOption(id=$id, opt1='$opt1')"
  }
}
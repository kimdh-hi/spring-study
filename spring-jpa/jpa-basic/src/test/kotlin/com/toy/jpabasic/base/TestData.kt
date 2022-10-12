package com.toy.jpabasic.base

import com.toy.jpabasic.domain.Group
import com.toy.jpabasic.domain.GroupOption
import com.toy.jpabasic.domain.Member

object TestData {
  val MEMBER_01 = Member(id = "member-01", name = "m1")

  val GROUP_01 = Group(id = "group-01", name = "g1")

  val GROUP_01_OPTION = GroupOption(groupId = "group-01", group = GROUP_01, false)
}
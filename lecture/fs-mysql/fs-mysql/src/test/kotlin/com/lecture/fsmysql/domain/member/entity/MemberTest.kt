package com.lecture.fsmysql.domain.member.entity

import com.lecture.fsmysql.utils.MemberFixtureFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class MemberTest {

  @Test
  fun `Member의 nickname을 변경할 수 있다`() {
    //given
    val member = MemberFixtureFactory.create()
    val changeName = "changeNickname"

    //when
    member.changeNickname(changeName)

    //then
    assertEquals(changeName, member.nickname)
  }

  @Test
  fun `Member의 nickname은 공백으로 변경할 수 없다`() {
    //given
    val member = MemberFixtureFactory.create()
    val changeName = " "

    //when
    member.changeNickname(changeName)

    //then
    assertNotEquals(changeName, member.nickname)
  }
}
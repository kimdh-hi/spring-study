package com.lecture.springbatchbasic.core.service

import com.lecture.springbatchbasic.core.domain.Member1VO
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Year

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MemberServiceTest {

  lateinit var memberService: MemberService

  @BeforeAll
  fun setup() {
    memberService = MemberService()
  }

  @Test
  fun classificationByAge() {
    //given
    val mockYear = mockk<Year>()
    every { mockYear.value } returns 2023
    mockkStatic(Year::class)
    every { Year.now() } returns mockYear

    val member1VO = mockk<Member1VO>()
    every { member1VO.id } returns 1L
    every { member1VO.age } returns 25
    every { member1VO.name } returns "name"

    //when
    val result = memberService.classificationByAge(member1VO)

    //then
    assertEquals("adult", result.type)
  }
}
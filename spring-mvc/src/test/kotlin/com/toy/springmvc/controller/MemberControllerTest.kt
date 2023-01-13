package com.toy.springmvc.controller

import com.toy.springmvc.domain.Member
import com.toy.springmvc.repository.MemberRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
internal class MemberControllerTest(
  private val mockMvc: MockMvc,
  private val memberRepository: MemberRepository
) {

  @Test
  fun get() {
    //given
    val member = memberRepository.save(Member(name = "member-name"))

    //then
    mockMvc.get("/member/${member.id!!}")
      .andDo { print() }
      .andExpect { jsonPath("$.name") { value(member.name) } }
  }
}
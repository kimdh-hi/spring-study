package com.toy.springmvc.controller

import com.toy.springmvc.domain.Member
import com.toy.springmvc.repository.MemberRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class PageableControllerTest @Autowired constructor(
  private val mockMvc: MockMvc,
  private val memberRepository: MemberRepository
) {

  @Test
  fun test() {
    //given
    (1..10).map { Member(name = "name$it").also { memberRepository.save(it) } }

    //when
    val result = mockMvc.get("/pageable") {
      param("page", "1")
      param("size", "3")
    }

    //then
    println(result.andReturn().response.contentAsString)
  }
}
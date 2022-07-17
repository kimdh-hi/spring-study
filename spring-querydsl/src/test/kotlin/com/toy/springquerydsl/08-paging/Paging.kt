package com.toy.springquerydsl.`08-paging`

import com.toy.springquerydsl.`00-base`.TestBase
import com.toy.springquerydsl.repository.MemberRepository
import com.toy.springquerydsl.vo.MemberSearchVO
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest

class Paging(
  val memberRepository: MemberRepository
): TestBase() {

  @Test
  fun `deprecated way 1`() {
    val pageable = PageRequest.of(0, 10)
    val searchVO = MemberSearchVO(username = "member")

    val page = memberRepository.searchV0deprecated(pageable, searchVO)

    println(page.content)
  }

  // PageableExecutionUtils - count 쿼리 최적화
  // content의 크기가 size보다 작은 경우 totalCountQuery 생략
  @Test
  fun `deprecated way 2`() {
    val pageable = PageRequest.of(0, 100)
    val searchVO = MemberSearchVO(username = "member")

    val page = memberRepository.searchV1deprecated(pageable, searchVO)

    println(page.content)
  }
}
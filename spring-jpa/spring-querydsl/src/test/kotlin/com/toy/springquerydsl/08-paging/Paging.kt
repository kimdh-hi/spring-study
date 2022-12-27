package com.toy.springquerydsl.`08-paging`

import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.toy.springquerydsl.`00-base`.BaseTest
import com.toy.springquerydsl.domain.QMember
import com.toy.springquerydsl.repository.MemberRepository
import com.toy.springquerydsl.vo.MemberSearchVO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.data.domain.PageRequest
import org.springframework.data.querydsl.QPageRequest
import org.springframework.data.querydsl.QSort

class Paging(
  val memberRepository: MemberRepository
): BaseTest() {

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

  @Test
  fun `searchV1`() {
    val pageable = QPageRequest.of(0, 3, QSort.by(OrderSpecifier(Order.DESC, QMember.member.username)))
    val searchVO = MemberSearchVO(username = "member")

    val page = memberRepository.search(pageable, searchVO)

    val content = page.content
    println(content)
    assertAll({
      assertEquals(0, page.number)
      content.forEach {
        assertTrue(it.username.contains("member"))
      }
    })
  }

  @Test
  fun `searchV2`() {
    val pageable = QPageRequest.of(0, 3, QSort.by(OrderSpecifier(Order.ASC, QMember.member.username)))
    val searchVO = MemberSearchVO(username = "member")

    val page = memberRepository.searchV2(pageable, searchVO)

    val content = page.content
    println(content)
    assertAll({
      assertEquals(0, page.number)
      content.forEach {
        assertTrue(it.username.contains("member"))
      }
    })
  }
}
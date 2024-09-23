package com.toy.springquerydsl.repository

import com.toy.springquerydsl.base.BaseTest
import com.toy.springquerydsl.vo.MemberSearchVO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.querydsl.QPageRequest

class MemberRepositoryImplTest(
  private val memberRepository: MemberRepository,
) : BaseTest() {

  @Test
  fun searchV3() {
    val result = memberRepository.searchV3(QPageRequest.of(0, 2), MemberSearchVO())
    assertThat(result.content.size).isEqualTo(2)
  }

  @Test
  @DisplayName("전체 조회 건수보다 pageSize 가 큰 경우 count query x")
  fun searchV3WithoutCountQuery1() {
    val result = memberRepository.searchV3(QPageRequest.of(0, 100), MemberSearchVO())
    println(result)
  }
}

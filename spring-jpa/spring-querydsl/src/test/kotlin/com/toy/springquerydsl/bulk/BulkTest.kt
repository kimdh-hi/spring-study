package com.toy.springquerydsl.bulk

import com.toy.springquerydsl.base.BaseTest
import com.toy.springquerydsl.domain.QMember.member
import com.toy.springquerydsl.repository.MemberRepository
import org.junit.jupiter.api.Test

class BulkTest(
  private val memberRepository: MemberRepository
): BaseTest() {

  /**
   * 영속성 컨텍스트를 무시? 하는 벌크 쿼리는 조심해서 사용하자.
   * DB의 상태와 영속성 컨텍스트 상태가 달라질 수 있다.
   */
  @Test
  fun bulkUpdate() {
    val count = query.update(member)
      .set(member.username, "update...")
      .execute()
    println(count)

    flushAndClear()
    val updatedMembers = query.selectFrom(member)
      .fetch()
    updatedMembers.forEach { println(it.username) }
  }

  /**
   * append, add, multiply 등...  기존 필드값을 기반으로 변경할 수 있는 여러 메서드도 제공된다.
   */
  @Test
  fun bulkUpdate2() {
    val count = query.update(member)
      .set(member.username, member.username.append("update~"))
      .execute()
    println(count)

    flushAndClear()
    val updatedMembers = query.selectFrom(member)
      .fetch()
    updatedMembers.forEach { println(it.username) }
  }

  @Test
  fun bulkDelete() {
    val count = query.delete(member)
      .where(member.age.goe(20))
      .execute()

    println(count)
  }
}

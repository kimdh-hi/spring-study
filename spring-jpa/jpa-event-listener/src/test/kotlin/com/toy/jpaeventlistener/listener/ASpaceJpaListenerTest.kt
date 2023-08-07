package com.toy.jpaeventlistener.listener

import com.toy.jpaeventlistener.domain.ASpace
import com.toy.jpaeventlistener.domain.BSpace
import com.toy.jpaeventlistener.domain.Member
import com.toy.jpaeventlistener.domain.MemberSpaceType
import com.toy.jpaeventlistener.repository.ASpaceRepository
import com.toy.jpaeventlistener.repository.BSpaceRepository
import com.toy.jpaeventlistener.repository.MemberRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class ASpaceJpaListenerTest @Autowired constructor(
  private val aSpaceRepository: ASpaceRepository,
  private val bSpaceRepository: BSpaceRepository,
  private val memberRepository: MemberRepository
) {

  @Test
  fun `preRemove2`() {
    //given
    val aSpace = ASpace(name = "a").also { aSpaceRepository.save(it) }
    val bSpace = BSpace(name = "b").also { bSpaceRepository.save(it) }
    val member1 = Member(name = "member1").also {
      it.entrySpaceId = aSpace.id!!
      it.entrySpaceType = MemberSpaceType.TYPE_A
      it.portalSpaceId = bSpace.id!!
      it.portalSpaceType = MemberSpaceType.TYPE_B
      memberRepository.save(it)
    }
    val member2 = Member(name = "member2").also {
      it.entrySpaceId = aSpace.id!!
      it.entrySpaceType = MemberSpaceType.TYPE_A
      it.portalSpaceId = bSpace.id!!
      it.portalSpaceType = MemberSpaceType.TYPE_B
      memberRepository.save(it)
    }

    //when
    aSpaceRepository.delete(aSpace)

    //then
    val afterMember1 = memberRepository.findByIdOrNull(member1.id)!!
    val afterMember2 = memberRepository.findByIdOrNull(member2.id)!!
    assertAll({
      assertNull(afterMember1.entrySpaceId)
      assertNull(afterMember1.entrySpaceType)
      assertNull(afterMember1.portalSpaceId)
      assertNull(afterMember1.portalSpaceType)

      assertNull(afterMember2.entrySpaceId)
      assertNull(afterMember2.entrySpaceType)
      assertNull(afterMember2.portalSpaceId)
      assertNull(afterMember2.portalSpaceType)
    })
  }

  @Test
  fun preRemove() {
    //given
    val aSpace = ASpace(name = "a").also { aSpaceRepository.save(it) }

    //when
    aSpaceRepository.delete(aSpace)
  }
}
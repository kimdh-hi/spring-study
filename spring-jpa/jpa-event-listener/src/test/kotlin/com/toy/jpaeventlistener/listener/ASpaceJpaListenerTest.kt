package com.toy.jpaeventlistener.listener

import com.toy.jpaeventlistener.domain.ASpace
import com.toy.jpaeventlistener.domain.Member
import com.toy.jpaeventlistener.domain.MemberSpaceType
import com.toy.jpaeventlistener.repository.ASpaceRepository
import com.toy.jpaeventlistener.repository.MemberRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class ASpaceJpaListenerTest @Autowired constructor(
  private val aSpaceRepository: ASpaceRepository,
  private val memberRepository: MemberRepository
) {

  @Test
  fun `preRemove2`() {
    //given
    val aSpace = ASpace(name = "a").also { aSpaceRepository.save(it) }
    val member1 = Member(name = "member1").also {
      it.entrySpaceId = aSpace.id!!
      it.entrySpaceType = MemberSpaceType.TYPE_A
      memberRepository.save(it)
    }
    val member2 = Member(name = "member2").also {
      it.entrySpaceId = aSpace.id!!
      it.entrySpaceType = MemberSpaceType.TYPE_A
      memberRepository.save(it)
    }

    //when
    aSpaceRepository.delete(aSpace)
  }

  @Test
  fun preRemove() {
    //given
    val aSpace = ASpace(name = "a").also { aSpaceRepository.save(it) }

    //when
    aSpaceRepository.delete(aSpace)
  }
}
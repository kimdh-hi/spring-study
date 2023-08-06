package com.toy.jpaeventlistener.listener

import com.toy.jpaeventlistener.domain.ASpace
import com.toy.jpaeventlistener.domain.MemberSpaceType
import com.toy.jpaeventlistener.repository.MemberRepository
import jakarta.persistence.PreRemove
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ASpaceJpaListener {

  @Autowired
  @Lazy
  lateinit var memberRepository: MemberRepository

  private val log = LoggerFactory.getLogger(javaClass)

  @PreRemove
  @Transactional
  fun preRemove(aSpace: ASpace) {
    log.info("aSpace preRemove called...")
    val members = memberRepository.findAllByEntrySpaceIdAndEntrySpaceType(aSpace.id!!, MemberSpaceType.TYPE_A)
    members.forEach {
      log.info("aSpace preRemove - clearEntrySpace $it")
      it.clearEntrySpace()
    }
  }
}
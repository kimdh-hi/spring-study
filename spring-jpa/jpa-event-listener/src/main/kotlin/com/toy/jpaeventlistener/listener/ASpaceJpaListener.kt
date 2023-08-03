package com.toy.jpaeventlistener.listener

import com.toy.jpaeventlistener.domain.ASpace
import com.toy.jpaeventlistener.repository.MemberRepository
import jakarta.persistence.PreRemove
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ASpaceJpaListener {

  @Autowired
  lateinit var memberRepository: MemberRepository

  private val log = LoggerFactory.getLogger(javaClass)

  @PreRemove
  fun preRemove(aSpace: ASpace) {
    log.info("aSpace preRemove called...")

  }
}
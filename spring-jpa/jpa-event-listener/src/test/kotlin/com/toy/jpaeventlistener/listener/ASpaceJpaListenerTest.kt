package com.toy.jpaeventlistener.listener

import com.toy.jpaeventlistener.domain.ASpace
import com.toy.jpaeventlistener.repository.ASpaceRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class ASpaceJpaListenerTest @Autowired constructor(
  private val aSpaceRepository: ASpaceRepository
) {

  @Test
  fun preRemove() {
    //given
    val aSpace = ASpace(name = "a").also { aSpaceRepository.save(it) }

    //when
    aSpaceRepository.delete(aSpace)
  }
}
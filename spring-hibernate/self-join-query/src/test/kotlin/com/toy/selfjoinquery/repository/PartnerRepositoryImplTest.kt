package com.toy.selfjoinquery.repository

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.querydsl.QPageRequest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class PartnerRepositoryImplTest(
  val partnerRepository: PartnerRepository
) {

  @Test
  fun getChildPartnerIds() {
    //when
    val childPartnerIds = partnerRepository.getChildPartnerIds("root-p-1")

    //then
    println(childPartnerIds)
  }

  @Test
  fun search() {
    val page = partnerRepository.search("root-p-1", QPageRequest.of(0,10))

    println(page.content)
  }
}
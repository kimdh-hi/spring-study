package com.toy.jpahierarchy.repository

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PartnerRepositoryTest(val partnerRepository: PartnerRepository) {

}
package com.toy.jpabasic.repository

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class EmbeddableTestEntityRepositoryTest @Autowired constructor(
    private val embeddableTestEntityRepository: EmbeddableTestEntityRepository
) {

    @Test
    fun test() {
        val entity1 = embeddableTestEntityRepository.findByIdOrNull("et1")!!
        val entity2 = embeddableTestEntityRepository.findByIdOrNull("et2")!!

        assertAll({
            assertDoesNotThrow { entity1.embeddableSample.isNotNull() }
            assertDoesNotThrow { entity2.embeddableSample.isNotNull() }
        })
    }
}

package com.example.ex.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
@DataJpaTest
internal class MemberRepositoryTest @Autowired constructor(private val memberRepository: MemberRepository) {

    @Test
    fun test() {
        val member = memberRepository.findById(1).get()
        println("member=${member.id}")
        assertEquals(1, memberRepository.count())
    }
}
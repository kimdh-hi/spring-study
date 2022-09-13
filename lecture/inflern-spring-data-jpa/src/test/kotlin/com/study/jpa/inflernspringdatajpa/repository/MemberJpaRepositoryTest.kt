package com.study.jpa.inflernspringdatajpa.repository

import com.study.jpa.inflernspringdatajpa.entity.Member
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional

@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class MemberJpaRepositoryTest(val memberJpaRepository: MemberJpaRepository) {

    @Test
    fun saveMember() {
        //given
        val member = Member(username = "username")

        //when
        val savedMember = memberJpaRepository.save(member)

        //then
        assertNotNull(savedMember.id)
    }

    @Test
    fun findMember() {
        //given
        val member = Member(username = "username")
        val savedMember = memberJpaRepository.save(member)

        //when
        val findMember = memberJpaRepository.find(savedMember.id!!)

        //then
        assertEquals(savedMember.id, findMember!!.id)
    }

}
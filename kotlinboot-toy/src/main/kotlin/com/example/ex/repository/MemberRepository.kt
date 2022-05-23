package com.example.ex.repository

import com.example.ex.domain.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Long> {

    fun findAllByUsername(username: String, pageable: Pageable): Page<Member>
}
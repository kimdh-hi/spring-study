package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.EmailAuthentication
import org.springframework.data.jpa.repository.JpaRepository

interface EmailAuthenticationRepository: JpaRepository<EmailAuthentication, String>
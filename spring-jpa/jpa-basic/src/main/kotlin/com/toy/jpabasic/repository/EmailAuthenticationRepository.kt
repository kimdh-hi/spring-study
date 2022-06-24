package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.EmailAuthentication
import org.springframework.data.repository.CrudRepository

interface EmailAuthenticationRepository: CrudRepository<EmailAuthentication, String>
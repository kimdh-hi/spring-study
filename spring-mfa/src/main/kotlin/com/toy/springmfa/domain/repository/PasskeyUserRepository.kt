package com.toy.springmfa.domain.repository

import com.toy.springmfa.domain.model.PasskeyUser
import org.springframework.data.jpa.repository.JpaRepository

interface PasskeyUserRepository : JpaRepository<PasskeyUser, String> {

  fun findByUsername(username: String): PasskeyUser?
}

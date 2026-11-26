package com.toy.springmfa.domain.repository

import com.toy.springmfa.domain.model.PasskeyCredential
import org.springframework.data.jpa.repository.JpaRepository

interface PasskeyCredentialRepository : JpaRepository<PasskeyCredential, String> {

  fun findByUserEntityUserId(userEntityUserId: String): List<PasskeyCredential>
}

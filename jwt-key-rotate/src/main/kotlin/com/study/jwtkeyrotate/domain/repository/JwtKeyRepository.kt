package com.study.jwtkeyrotate.domain.repository

import com.study.jwtkeyrotate.domain.model.JwtKey
import com.study.jwtkeyrotate.domain.model.enums.JwtKeyStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface JwtKeyRepository : JpaRepository<JwtKey, String> {
  fun findByStatus(status: JwtKeyStatus): JwtKey?
  fun deleteByStatusAndDemotedAtBefore(status: JwtKeyStatus, before: Instant)
}

package com.toy.dpopproofjwt.domain.repository

import com.toy.dpopproofjwt.domain.model.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
  fun findByTokenHash(tokenHash: String): RefreshToken?

  @Modifying(flushAutomatically = true, clearAutomatically = true)
  @Query("delete from RefreshToken r where r.tokenHash = :tokenHash")
  fun deleteByTokenHash(tokenHash: String): Int
}

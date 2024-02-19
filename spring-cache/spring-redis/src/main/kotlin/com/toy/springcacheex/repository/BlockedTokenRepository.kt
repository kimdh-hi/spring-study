package com.toy.springcacheex.repository

import com.toy.springcacheex.domain.BlockedToken
import org.springframework.data.repository.CrudRepository

interface BlockedTokenRepository: CrudRepository<BlockedToken, String> {
  fun findAllByUserId(userId: String): List<BlockedToken>
}
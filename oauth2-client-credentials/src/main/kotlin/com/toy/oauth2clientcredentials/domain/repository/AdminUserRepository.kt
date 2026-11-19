package com.toy.oauth2clientcredentials.domain.repository

import com.toy.oauth2clientcredentials.domain.model.AdminUser
import org.springframework.data.jpa.repository.JpaRepository

interface AdminUserRepository : JpaRepository<AdminUser, Long> {
  fun findByUsername(username: String): AdminUser?
}

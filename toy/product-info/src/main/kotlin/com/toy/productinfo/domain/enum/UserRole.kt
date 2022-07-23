package com.toy.productinfo.domain.enum

import org.springframework.security.core.GrantedAuthority

enum class UserRole(val role: String): GrantedAuthority {
  USER("ROLE_USER"),
  CORPORATE("ROLE_CORPORATE");

  override fun getAuthority() = role
}

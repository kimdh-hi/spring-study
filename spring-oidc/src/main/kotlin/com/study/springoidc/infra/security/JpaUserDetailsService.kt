package com.study.springoidc.infra.security

import com.study.springoidc.application.UserService
import org.springframework.security.core.userdetails.User as SecurityUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JpaUserDetailsService(
  private val userService: UserService,
) : UserDetailsService {

  override fun loadUserByUsername(username: String): UserDetails {
    val user = runCatching { userService.getByUsername(username) }
      .getOrElse { throw UsernameNotFoundException(username) }

    return SecurityUser.builder()
      .username(user.username)
      .password(user.password)
      .roles(*user.roleList.toTypedArray())
      .build()
  }
}

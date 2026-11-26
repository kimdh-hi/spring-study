package com.toy.springmfa.infra.security

import com.toy.springmfa.domain.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class JpaUserDetailsService(
  private val userRepository: UserRepository,
) : UserDetailsService {

  override fun loadUserByUsername(username: String): UserDetails {
    val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
    return User.withUsername(user.username)
      .password(user.password)
      .authorities(*user.roleList().toTypedArray())
      .build()
  }
}

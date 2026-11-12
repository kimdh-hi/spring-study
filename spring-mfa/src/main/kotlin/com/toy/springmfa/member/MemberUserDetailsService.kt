package com.toy.springmfa.member

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberUserDetailsService(
  private val memberRepository: MemberRepository,
) : UserDetailsService {

  override fun loadUserByUsername(username: String): UserDetails {
    val member = memberRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
    return User.withUsername(member.username)
      .password(member.password)
      .roles(*member.roleList().toTypedArray())
      .build()
  }
}

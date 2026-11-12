package com.toy.springmfa.member

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MemberInitializer(
  private val memberRepository: MemberRepository,
  private val passwordEncoder: PasswordEncoder,
) : ApplicationRunner {

  @Transactional
  override fun run(args: ApplicationArguments) {
    if (memberRepository.count() > 0) return
    memberRepository.saveAll(
      listOf(
        Member("user", passwordEncoder.encode("password")!!, "USER"),
        Member("admin", passwordEncoder.encode("password")!!, "USER,ADMIN"),
      ),
    )
  }
}

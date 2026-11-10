package com.toy.dpopproofjwt.application.auth

import com.toy.dpopproofjwt.common.exception.InvalidCredentialsException
import com.toy.dpopproofjwt.domain.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LoginService(
  private val userRepository: UserRepository,
  private val passwordEncoder: PasswordEncoder,
  private val proofVerifier: DPoPProofVerifier,
  private val refreshTokenService: RefreshTokenService,
) {

  @Transactional
  fun login(command: LoginCommand): IssuedToken {
    val user = userRepository.findByUsername(command.username)
      ?.takeIf { passwordEncoder.matches(command.password, it.password) }
      ?: throw InvalidCredentialsException()

    val jkt = proofVerifier.verify(command.proof)
    return refreshTokenService.issue(user.username, jkt)
  }
}

data class LoginCommand(
  val username: String,
  val password: String,
  val proof: DPoPProof,
)

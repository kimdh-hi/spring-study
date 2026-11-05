package com.toy.dpopproofjwt.application.auth

import com.toy.dpopproofjwt.common.exception.InvalidCredentialsException
import com.toy.dpopproofjwt.domain.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LoginService(
  private val userRepository: UserRepository,
  private val passwordEncoder: PasswordEncoder,
  private val tokenIssuer: TokenIssuer,
) {

  fun login(command: LoginCommand): IssuedToken {
    val user = userRepository.findByUsername(command.username)
      ?.takeIf { passwordEncoder.matches(command.password, it.password) }
      ?: throw InvalidCredentialsException()

    return tokenIssuer.issue(user.username, command.dPoPProof, command.method, command.targetUri)
  }
}

data class LoginCommand(
  val username: String,
  val password: String,
  val dPoPProof: String,
  val method: String,
  val targetUri: String,
)

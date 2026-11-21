package com.toy.oauth2clientcredentials.application.admin

import com.toy.oauth2clientcredentials.common.exception.InvalidCredentialsException
import com.toy.oauth2clientcredentials.domain.repository.AdminUserRepository
import com.toy.oauth2clientcredentials.infra.security.admin.AdminTokenIssuer
import com.toy.oauth2clientcredentials.infra.security.admin.IssuedToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdminLoginService(
  private val adminUserRepository: AdminUserRepository,
  private val passwordEncoder: PasswordEncoder,
  private val adminTokenIssuer: AdminTokenIssuer,
) {

  fun login(username: String, password: String): IssuedToken {
    val admin = adminUserRepository.findByUsername(username)
    if (admin == null || !passwordEncoder.matches(password, admin.password)) {
      throw InvalidCredentialsException("invalid username or password")
    }
    return adminTokenIssuer.issue(admin.username)
  }
}

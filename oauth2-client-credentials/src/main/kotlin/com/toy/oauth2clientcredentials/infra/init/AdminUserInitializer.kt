package com.toy.oauth2clientcredentials.infra.init

import com.toy.oauth2clientcredentials.domain.model.AdminUser
import com.toy.oauth2clientcredentials.domain.repository.AdminUserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminUserInitializer(
  private val adminUserRepository: AdminUserRepository,
  private val passwordEncoder: PasswordEncoder,
) : ApplicationRunner {

  override fun run(args: ApplicationArguments) {
    if (adminUserRepository.findByUsername("admin") == null) {
      adminUserRepository.save(AdminUser("admin", passwordEncoder.encode("password")!!))
    }
  }
}

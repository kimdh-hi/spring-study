package com.study.springgraalvm.application

import com.study.springgraalvm.domain.model.User
import com.study.springgraalvm.domain.repository.CompanyRepository
import com.study.springgraalvm.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
  private val userRepository: UserRepository,
  private val companyRepository: CompanyRepository,
) {
  fun create(companyId: Long, name: String, email: String): User {
    val company = companyRepository.findById(companyId)
      ?: throw NoSuchElementException("company not found: $companyId")
    return userRepository.save(User(name = name, email = email, company = company))
  }

  @Transactional(readOnly = true)
  fun findByCompany(companyId: Long): List<User> = userRepository.findByCompanyId(companyId)
}

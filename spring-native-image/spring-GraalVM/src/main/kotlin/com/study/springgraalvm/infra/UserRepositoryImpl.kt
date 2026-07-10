package com.study.springgraalvm.infra

import com.study.springgraalvm.domain.model.User
import com.study.springgraalvm.domain.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
  private val jpa: UserJpaRepository,
) : UserRepository {
  override fun save(user: User): User = jpa.save(user)
  override fun findById(id: Long): User? = jpa.findByIdOrNull(id)
  override fun findByCompanyId(companyId: Long): List<User> = jpa.findByCompanyId(companyId)
}

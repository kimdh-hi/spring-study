package com.study.springgraalvm.domain.repository

import com.study.springgraalvm.domain.model.User

interface UserRepository {
  fun save(user: User): User
  fun findById(id: Long): User?
  fun findByCompanyId(companyId: Long): List<User>
}

package com.study.springgraalvm.infra

import com.study.springgraalvm.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<User, Long> {
  fun findByCompanyId(companyId: Long): List<User>
}

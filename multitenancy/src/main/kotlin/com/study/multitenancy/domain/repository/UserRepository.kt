package com.study.multitenancy.domain.repository

import com.study.multitenancy.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>

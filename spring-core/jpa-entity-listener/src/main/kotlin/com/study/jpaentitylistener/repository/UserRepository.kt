package com.study.jpaentitylistener.repository

import com.study.jpaentitylistener.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String>

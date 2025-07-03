package com.study.jpacore.repository

import com.study.jpacore.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String>

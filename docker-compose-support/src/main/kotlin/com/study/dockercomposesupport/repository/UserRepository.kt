package com.study.dockercomposesupport.repository

import com.study.dockercomposesupport.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String>

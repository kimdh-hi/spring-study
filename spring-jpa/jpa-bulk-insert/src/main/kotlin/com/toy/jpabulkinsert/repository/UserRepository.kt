package com.toy.jpabulkinsert.repository

import com.toy.jpabulkinsert.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String>
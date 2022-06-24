package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String>
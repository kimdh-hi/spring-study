package com.toy.springkotlin.repository

import com.toy.springkotlin.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String>

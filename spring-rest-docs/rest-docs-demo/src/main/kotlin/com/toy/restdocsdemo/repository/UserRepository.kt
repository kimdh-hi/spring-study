package com.toy.restdocsdemo.repository

import com.toy.restdocsdemo.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long>
package com.toy.kotlinjdsl.repository

import com.toy.kotlinjdsl.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String>
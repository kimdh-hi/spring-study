package com.toy.springflyway.repository

import com.toy.springflyway.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {
}
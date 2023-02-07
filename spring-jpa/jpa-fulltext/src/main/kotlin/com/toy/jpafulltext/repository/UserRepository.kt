package com.toy.jpafulltext.repository

import com.toy.jpafulltext.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String>, UserRepositoryCustom

interface UserRepositoryCustom {

  fun searchByDescription(description: String): List<User>
}
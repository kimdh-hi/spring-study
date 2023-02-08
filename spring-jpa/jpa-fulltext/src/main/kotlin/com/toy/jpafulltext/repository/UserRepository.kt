package com.toy.jpafulltext.repository

import com.toy.jpafulltext.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String>, UserRepositoryCustom

interface UserRepositoryCustom {

  fun searchByDescriptionV1(description: String): List<User>

  fun searchByDescriptionV2(description: String): List<User>
}
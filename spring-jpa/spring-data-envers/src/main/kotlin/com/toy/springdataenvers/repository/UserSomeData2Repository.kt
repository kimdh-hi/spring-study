package com.toy.springdataenvers.repository

import com.toy.springdataenvers.domain.UserSomeData2
import org.springframework.data.repository.CrudRepository

interface UserSomeData2Repository: CrudRepository<UserSomeData2, String> {
  fun findByUserId(userId: String): UserSomeData2?
}
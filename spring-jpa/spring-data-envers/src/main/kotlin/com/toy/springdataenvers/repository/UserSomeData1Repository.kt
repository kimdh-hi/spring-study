package com.toy.springdataenvers.repository

import com.toy.springdataenvers.domain.UserSomeData1
import org.springframework.data.repository.CrudRepository

interface UserSomeData1Repository: CrudRepository<UserSomeData1, String> {
  fun findByUserId(userId: String): UserSomeData1?
}
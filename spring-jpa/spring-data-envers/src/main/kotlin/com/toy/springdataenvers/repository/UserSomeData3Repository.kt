package com.toy.springdataenvers.repository

import com.toy.springdataenvers.domain.UserSomeData3
import org.springframework.data.jpa.repository.JpaRepository

interface UserSomeData3Repository: JpaRepository<UserSomeData3, String> {

  fun findByUserId(userId: String): UserSomeData3?
}
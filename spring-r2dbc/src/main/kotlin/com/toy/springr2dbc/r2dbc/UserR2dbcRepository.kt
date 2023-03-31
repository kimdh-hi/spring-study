package com.toy.springr2dbc.r2dbc

import com.toy.springr2dbc.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserR2dbcRepository: CoroutineCrudRepository<User, String> {

}
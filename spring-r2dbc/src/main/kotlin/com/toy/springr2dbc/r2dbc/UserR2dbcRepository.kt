package com.toy.springr2dbc.r2dbc

import com.toy.springr2dbc.domain.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserR2dbcRepository: CoroutineCrudRepository<User, String> {

}
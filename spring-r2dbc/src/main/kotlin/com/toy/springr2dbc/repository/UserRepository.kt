package com.toy.springr2dbc.repository

import com.toy.springr2dbc.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {
}
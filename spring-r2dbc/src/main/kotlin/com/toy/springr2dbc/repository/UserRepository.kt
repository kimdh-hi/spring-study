package com.toy.springr2dbc.repository

import com.toy.springr2dbc.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface UserRepository: JpaRepository<User, String> {
}
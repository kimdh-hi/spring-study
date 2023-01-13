package com.toy.jparoutingdatasource.repository

import com.toy.jparoutingdatasource.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepositry: CrudRepository<User, String> {
}
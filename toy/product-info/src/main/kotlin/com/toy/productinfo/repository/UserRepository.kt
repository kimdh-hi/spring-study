package com.toy.productinfo.repository

import com.toy.productinfo.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String> {

}
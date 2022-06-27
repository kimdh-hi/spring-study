package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.User
import com.toy.jpabasic.vo.UserListResponseVO
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, String>, UserRepositoryCustom

interface UserRepositoryCustom {

  fun searchList(): MutableList<UserListResponseVO>

  fun searchListV2(): MutableList<User>
}
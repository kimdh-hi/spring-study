package com.toy.webfluxr2dbcpostgres.service

import com.toy.webfluxr2dbcpostgres.domain.User
import com.toy.webfluxr2dbcpostgres.repository.UserRepository
import com.toy.webfluxr2dbcpostgres.vo.UserSaveRequestVO
import com.toy.webfluxr2dbcpostgres.vo.UserSaveResponseVO
import com.toy.webfluxr2dbcpostgres.vo.UserUpdateRequestVO
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class UserService(
  private val userRepository: UserRepository
) {

  suspend fun get(id: Long): User {
    return userRepository.findById(id) ?: throw IllegalArgumentException("user not found ...")
  }

  fun list(): Flow<User> {
    val users = userRepository.findAll()

    return users
  }

  suspend fun save(requestVO: UserSaveRequestVO): UserSaveResponseVO {
    val user = requestVO.toEntity()
    val savedUser = userRepository.save(user)
    return UserSaveResponseVO.of(savedUser)
  }

  suspend fun update(id: Long, requestVO: UserUpdateRequestVO) {
    val user = userRepository.findById(id) ?: throw IllegalArgumentException("user not found ...")
    user.update(requestVO.name, requestVO.username, requestVO.password)

    userRepository.save(user)
  }
}
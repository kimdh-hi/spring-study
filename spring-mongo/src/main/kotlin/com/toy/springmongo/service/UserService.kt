package com.toy.springmongo.service

import com.toy.springmongo.domain.User
import com.toy.springmongo.repository.UserRepository
import com.toy.springmongo.vo.UserRequestVO
import com.toy.springmongo.vo.UserResponseVO
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
  private val userRepository: UserRepository,
  private val mongoTemplate: MongoTemplate
) {

  fun save(vo: UserRequestVO): UserResponseVO {
    val entity = vo.toEntity()
    val user = userRepository.save(entity)
    return UserResponseVO.of(user)
  }

  fun find(id: String): UserResponseVO {
    val user = userRepository.findByIdOrNull(id) ?: throw RuntimeException("not found...")
    return UserResponseVO.of(user)
  }

  fun findAll(): List<UserResponseVO> {
    return userRepository.findAll()
      .map { UserResponseVO.of(it) }
  }

  fun delete(id: String) {
    val user = userRepository.findByIdOrNull(id) ?: throw RuntimeException("not found...")
    userRepository.delete(user)
  }

  fun update(id: String, vo: UserRequestVO): UserResponseVO {
    val user = userRepository.findByIdOrNull(id) ?: throw RuntimeException("not found...")
    user.update(vo.name)

    val updatedUser = userRepository.save(user)
    return UserResponseVO.of(updatedUser)
  }
}
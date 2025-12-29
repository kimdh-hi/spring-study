package com.study.jpacore.service

import com.study.jpacore.entity.User
import com.study.jpacore.repository.DeviceRepository
import com.study.jpacore.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
  private val userRepository: UserRepository,
  private val deviceRepository: DeviceRepository,
) {

  @Transactional
  fun save(name: String): User {
    return userRepository.save(User.of(name))
  }

  @Transactional(readOnly = true)
  fun getList(): List<User> {
    return userRepository.findAll()
  }

  @Transactional
  fun delete(userId: String) {
    val user = userRepository.findByIdOrNull(userId)!!
    userRepository.delete(user)
    deviceRepository.findUserByDeviceKey("key1")
  }
}

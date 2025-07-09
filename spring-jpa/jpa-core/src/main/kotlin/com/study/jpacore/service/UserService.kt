package com.study.jpacore.service

import com.study.jpacore.entity.User
import com.study.jpacore.repository.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService(
  private val userRepository: UserRepository,
) {

  private val log = LoggerFactory.getLogger(UserService::class.java)

  @Transactional
  fun save(name: String) {
    val userEntity = User.of(name)
    log.info("userEntity createdDate={}", userEntity.createdDate)

    Thread.sleep(2_000)

    val savedUser = userRepository.save(userEntity)
    log.info("saveUser createdDate={}", savedUser.createdDate)
  }
}

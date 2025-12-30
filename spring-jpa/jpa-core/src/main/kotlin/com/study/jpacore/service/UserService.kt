package com.study.jpacore.service

import com.study.jpacore.common.TransactionDelegator
import com.study.jpacore.common.runAfterCommit
import com.study.jpacore.entity.User
import com.study.jpacore.repository.DeviceRepository
import com.study.jpacore.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

@Service
class UserService(
  private val userRepository: UserRepository,
  private val deviceRepository: DeviceRepository,
) {

  private val log = LoggerFactory.getLogger(UserService::class.java)

  @Transactional
  fun save(name: String): User {
    val user = userRepository.save(User.of(name))

    TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
      override fun afterCommit() {
        log.debug("send welcome email...")
      }
    })

    runAfterCommit {
      log.debug("send welcome email...")
    }

    TransactionDelegator.runAfterCommit {
      log.debug("send welcome email...")
    }

    return user
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

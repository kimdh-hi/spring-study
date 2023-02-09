package com.toy.springdataenvers.service

import com.toy.springdataenvers.domain.User
import com.toy.springdataenvers.repository.UserRepository
import org.springframework.data.history.Revision
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserRevisionService(
  private val userRepository: UserRepository
) {

  // 모든 변경사항
  fun findRevisions(userId: String): List<User> {
    return userRepository.findRevisions(userId).content
      .map { it.entity }
  }

  // 마지막 변경사항
  fun findLastChangeRevision(userId: String): User {
    return userRepository.findLastChangeRevision(userId).get().entity
  }
}
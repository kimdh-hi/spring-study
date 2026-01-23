package com.study.archunit.user.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
  private val userreposi
) {

  @Transactional(readOnly = true)
  fun findAll() =
}

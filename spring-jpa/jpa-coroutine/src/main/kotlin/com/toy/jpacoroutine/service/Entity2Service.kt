package com.toy.jpacoroutine.service

import com.toy.jpacoroutine.repository.Entity2Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class Entity2Service(
  private val entity2Repository: Entity2Repository
) {
  fun findAll() = entity2Repository.findAll()
}
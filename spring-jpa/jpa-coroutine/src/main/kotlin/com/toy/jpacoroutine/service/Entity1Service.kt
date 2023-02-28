package com.toy.jpacoroutine.service

import com.toy.jpacoroutine.repository.Entity1Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class Entity1Service(
  private val entity1Repository: Entity1Repository
) {
  fun findAll() = entity1Repository.findAll()

}
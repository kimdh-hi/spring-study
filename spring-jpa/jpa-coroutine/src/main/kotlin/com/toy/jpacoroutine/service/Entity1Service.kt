package com.toy.jpacoroutine.service

import com.toy.jpacoroutine.domain.Entity1
import com.toy.jpacoroutine.repository.Entity1Repository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class Entity1Service(
  private val entity1Repository: Entity1Repository
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun findAllBySearch(keyword: String): List<Entity1> {
    log.info("Entity1Service.findAllBySearch start")
    val result = entity1Repository.findAllByDataContaining(keyword)
    log.info("Entity1Service.findAllBySearch end")

    return result
  }

}
package com.toy.jpacoroutine.service

import com.toy.jpacoroutine.domain.Entity2
import com.toy.jpacoroutine.repository.Entity2Repository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class Entity2Service(
  private val entity2Repository: Entity2Repository
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun findAllBySearch(keyword: String): List<Entity2> {
    log.info("Entity2Service.findAllBySearch start")
    val result = entity2Repository.findAllByDataContaining(keyword)
    log.info("Entity2Service.findAllBySearch end")

    return result
  }
}
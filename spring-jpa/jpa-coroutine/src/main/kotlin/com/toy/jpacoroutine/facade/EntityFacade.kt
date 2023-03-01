package com.toy.jpacoroutine.facade

import com.toy.jpacoroutine.service.Entity1Service
import com.toy.jpacoroutine.service.Entity2Service
import com.toy.jpacoroutine.vo.EntityResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EntityFacade(
  private val entity1Service: Entity1Service,
  private val entity2Service: Entity2Service
) {

  private val log = LoggerFactory.getLogger(javaClass)

  // 비동기적으로 호출되긴 하지만 jpa 를 통한 db 접근이 블로킹이기 때문에 의미없음
  suspend fun findAllCoroutine() = withContext(Dispatchers.IO) {
    val entity1s = async {
      log.info("findAllCoroutine.findAll entity1s")
      withContext(Dispatchers.IO) {
        entity1Service.findAll()
      }
    }
    val entity2s = async {
      log.info("findAllCoroutine.findAll entity2s")
      withContext(Dispatchers.IO) {
        entity2Service.findAll()
      }
    }

    EntityResponse(
      entity1s.await()
        .apply { log.info("entity1.complete") },
      entity2s.await()
        .apply { log.info("entity2.complete") },
    )
  }

  fun findAll(): EntityResponse {
    val entity1s = entity1Service.findAll()
    val entity2s = entity2Service.findAll()

    return EntityResponse(entity1s, entity2s)
  }
}
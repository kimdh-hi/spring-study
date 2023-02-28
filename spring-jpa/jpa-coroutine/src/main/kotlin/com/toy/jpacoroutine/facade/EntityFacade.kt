package com.toy.jpacoroutine.facade

import com.toy.jpacoroutine.service.Entity1Service
import com.toy.jpacoroutine.service.Entity2Service
import com.toy.jpacoroutine.vo.EntityResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class EntityFacade(
  private val entity1Service: Entity1Service,
  private val entity2Service: Entity2Service
) {

  // db가 blocking 이면 의미없음.
  suspend fun findAllCoroutine() = withContext(Dispatchers.IO) {
    val entity1s = async {
      withContext(Dispatchers.IO) {
        entity1Service.findAll()
      }
    }
    val entity2s = async {
      withContext(Dispatchers.IO) {
        entity2Service.findAll()
      }
    }

    EntityResponse(entity1s.await(), entity2s.await())
  }

  fun findAll(): EntityResponse {
    val entity1s = entity1Service.findAll()
    val entity2s = entity2Service.findAll()

    return EntityResponse(entity1s, entity2s)
  }
}
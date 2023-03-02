package com.toy.jpacoroutine.facade

import com.toy.jpacoroutine.service.Entity1Service
import com.toy.jpacoroutine.service.Entity2Service
import com.toy.jpacoroutine.vo.EntityResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@Service
class EntityFacade(
  private val entity1Service: Entity1Service,
  private val entity2Service: Entity2Service
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @OptIn(ExperimentalTime::class)
  suspend fun findAllCoroutine(keyword: String) = withContext(Dispatchers.IO) {
    val timedValue = measureTimedValue {
      val entity1s = async {
        log.info("findAllCoroutine.findAll entity1s")
        withContext(Dispatchers.IO) {
          entity1Service.findAllBySearch(keyword)
        }
      }
      val entity2s = async {
        log.info("findAllCoroutine.findAll entity2s")
        withContext(Dispatchers.IO) {
          entity2Service.findAllBySearch(keyword)
        }
      }

      EntityResponse(
        entity1s.await()
          .apply { log.info("entity1.complete") },
        entity2s.await()
          .apply { log.info("entity2.complete") },
      )
    }
    log.info("elapsed time: ${timedValue.duration.inWholeMilliseconds}")
    timedValue.value
  }

  @OptIn(ExperimentalTime::class)
  fun findAll(keyword: String): EntityResponse {
    val timedValue = measureTimedValue {
      log.info("findAllCoroutine.findAll entity1s")
      val entity1s = entity1Service.findAllBySearch(keyword)
      log.info("entity1.complete")

      log.info("findAllCoroutine.findAll entity2s")
      val entity2s = entity2Service.findAllBySearch(keyword)
      log.info("entity2.complete")
      EntityResponse(entity1s, entity2s)
    }

    log.info("elapsed time: ${timedValue.duration.inWholeMilliseconds}")
    return timedValue.value
  }
}
package com.toy.redissondistributedlock.service

import com.toy.redissondistributedlock.repository.SpaceRepository
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.lang.Exception
import java.util.concurrent.TimeUnit

@Service
@Transactional
class SpaceService(
  private val spaceRepository: SpaceRepository,
  private val redissonClient: RedissonClient,
  private val transactionManager: PlatformTransactionManager,
) {
  private val log = LoggerFactory.getLogger(javaClass)

  fun participateLock(spaceId: String) {
    val rLock = redissonClient.getLock(spaceId)

    try {
      if(rLock.tryLock(10, 10, TimeUnit.SECONDS)) {
        val tx = transactionManager.getTransaction(DefaultTransactionDefinition())
        val space = spaceRepository.findByIdOrNull(spaceId) ?: throw RuntimeException("space not found")
        try {
          log.info("[Thread: ${Thread.currentThread().id}] user increase...")
          space.participate()
          spaceRepository.save(space)
          transactionManager.commit(tx)
        } catch (ex: Exception) {
          log.error("space participate error...")
        }
      }
    } catch (ex: InterruptedException) {
      log.warn("failed to get lock")
    } finally {
      if(rLock.isLocked && rLock.isHeldByCurrentThread)
        rLock.unlock()
    }
  }

  @Transactional
  fun participate(spaceId: String) {
    val space = spaceRepository.findByIdOrNull(spaceId) ?: throw RuntimeException("space not found")
    space.participate()
  }
}
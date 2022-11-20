package com.toy.redissondistributedlock.service

import com.toy.redissondistributedlock.amqp.consumer.SpaceUserCountAdjustMessage
import com.toy.redissondistributedlock.repository.SpaceRepository
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
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
  private val rabbitTemplate: RabbitTemplate
) {
  private val log = LoggerFactory.getLogger(javaClass)

  fun participateWithLock(spaceId: String) {
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

  fun participateWithLockAndAmqp(spaceId: String) {
    val rLock = redissonClient.getLock(spaceId)

    try {
      if(rLock.tryLock(10, 10, TimeUnit.SECONDS)) {
        val tx = transactionManager.getTransaction(DefaultTransactionDefinition())
        val space = spaceRepository.findByIdOrNull(spaceId) ?: throw RuntimeException("space not found")
        try {
          log.info("[Thread: ${Thread.currentThread().id}] user increase...")
          val savedSpace = spaceRepository.save(space)
          val message = SpaceUserCountAdjustMessage(savedSpace.id!!, true)
          rabbitTemplate.convertSendAndReceive("space.user-count", message)
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
  fun participateWithoutLock(spaceId: String) {
    val space = spaceRepository.findByIdOrNull(spaceId) ?: throw RuntimeException("space not found")
    space.participate()
  }

  @Transactional
  fun increaseSpaceUserCount(id: String) {
    val space = spaceRepository.findByIdOrNull(id) ?: throw RuntimeException("space not found")
    space.participate()
  }
}
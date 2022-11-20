package com.toy.redissondistributedlock.utils

import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.Serial
import java.io.Serializable
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

@Component
class LockUtils(
  private val redissonClient: RedissonClient
) {

  private val log = LoggerFactory.getLogger(javaClass)

  fun createKey(key: String, waitTime: Duration, leaseTime: Duration): LockKey? {
    return LockKey.of(key, waitTime, leaseTime)
  }

  fun createLock(lockKey: LockKey): Lock {
    val key: String = lockKey.key
    val lock = redissonClient.getLock(key)
    return Lock(lockKey, lock)
  }

  fun <T> synchronize(lock: Lock, task: Supplier<T>): T {
    try {
      log.debug("try lock [key: {}]", lock.key)
      if (lock.tryLock()) {
        log.debug("locking [key: {}]", lock.key)
        return task.get()
      }
    } catch (e: InterruptedException) {
      throw RuntimeException("failed to process locking task...", e)
    } finally {
      log.debug("release lock [key: {}]", lock.key)
      lock.release()
    }
    throw RuntimeException("timeout for process locking task...")
  }

  fun <T> synchronize(lock: Lock, task: Runnable): T {
    try {
      log.debug("try lock [key: {}]", lock.key)
      if (lock.tryLock()) {
        log.debug("locking [key: {}]", lock.key)
        task.run()
      }
    } catch (e: InterruptedException) {
      throw RuntimeException("failed to process locking task...", e)
    } finally {
      log.debug("release lock [key: {}]", lock.key)
      lock.release()
    }
    throw RuntimeException("timeout for process locking task...")
  }
}

data class LockKey(
  val key: String,
  val waitTime: Duration,
  val leaseTime: Duration
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 6280410954115007981L

    private const val PREFIX = "rlock-"

    fun of(key: String, waitTime: Duration, leaseTime: Duration): LockKey {
      return LockKey(
        key = "$PREFIX$key", waitTime = waitTime, leaseTime = leaseTime
      )
    }
  }

  fun getWaitTimeSeconds() = waitTime.seconds

  fun getLeaseTimeSeconds() = leaseTime.seconds
}

data class Lock(
  val key: LockKey,
  val rLock: RLock
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 6548146641010554518L
  }

  @Throws(InterruptedException::class)
  fun tryLock(): Boolean {
    val waitTime: Long = key.getWaitTimeSeconds()
    val leaseTime: Long = key.getLeaseTimeSeconds()
    return rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)
  }

  fun release() {
    if (rLock.isLocked && rLock.isHeldByCurrentThread) {
      rLock.unlock()
    }
  }
}
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

  fun createLock(lockKey: LockKey): Lock {
    val key: String = lockKey.name
    val lock = redissonClient.getLock(key)
    return Lock.of(lockKey, lock)
  }
}

data class LockKey(
  val name: String,
  val waitTimeSeconds: Long,
  val leaseTimeSeconds: Long
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 6280410954115007981L

    fun of(prefix: String, key: String, waitTimeSeconds: Long, leaseTimeSeconds: Long): LockKey {
      return LockKey(
        name = "$prefix$key", waitTimeSeconds = waitTimeSeconds, leaseTimeSeconds = leaseTimeSeconds
      )
    }
  }
}

data class Lock(
  val key: LockKey,
  val rLock: RLock
): Serializable {

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    @Serial
    private const val serialVersionUID: Long = 6548146641010554518L

    fun of(key: LockKey, rLock: RLock): Lock {
      return Lock(key = key, rLock = rLock)
    }
  }

  fun tryLock(): Boolean {
    val waitTime: Long = key.waitTimeSeconds
    val leaseTime: Long = key.leaseTimeSeconds

    return try {
      rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)
    } catch (e: InterruptedException) {
      throw e
    }
  }

  fun release() {
    if (rLock.isLocked && rLock.isHeldByCurrentThread) {
      log.debug("release caught lock [key: {}]", key.name)
      rLock.unlock()
    }
  }

  fun <T> synchronize(task: Supplier<T>): T {
    return try {
      log.debug("trying to catch a lock [key: {}]", key.name)
      if (tryLock()) {
        log.debug("caught the lock successfully [key: {}]", key.name)
        task.get()
      } else {
        throw RuntimeException("redisson lock error...")
      }
    } catch (e: InterruptedException) {
      throw RuntimeException("redisson lock error...")
    } finally {
      release()
    }
  }

  fun synchronize(task: Runnable) {
    synchronize<Unit> {
      task.run()
    }
  }
}
package com.study.jpacore.service

import com.study.jpacore.entity.User
import com.study.jpacore.repository.UserRepository
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import javax.sql.DataSource

@Service
class LazyConnectionTestService(
  private val dataSource: DataSource,
  private val userRepository: UserRepository,
) {

  private val log = LoggerFactory.getLogger(LazyConnectionTestService::class.java)

  @Transactional
  fun withoutDbTask(): Int {
    return activeConnections()
  }

  @Transactional
  fun withDbTask(): Pair<Int, Int> {
    val before = activeConnections()
    userRepository.save(User.of(UUID.randomUUID().toString()))
    userRepository.flush()
    return before to activeConnections()
  }

  private fun activeConnections(): Int {
    // unwrap delegates to the target DataSource when wrapped by LazyConnectionDataSourceProxy
    val activeConnections = dataSource.unwrap(HikariDataSource::class.java).hikariPoolMXBean.activeConnections
    log.info("hikariPool activeConnection={}", activeConnections)
    return activeConnections
  }
}

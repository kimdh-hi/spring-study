package com.study.jpacore.service

import com.study.jpacore.entity.User
import com.study.jpacore.repository.UserRepository
import com.zaxxer.hikari.HikariDataSource
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.stereotype.Service
import java.util.UUID
import javax.sql.DataSource

@Service
class LazyConnectionTestService(
  private val dataSource: DataSource,
  private val userRepository: UserRepository,
) {

  private val log = LoggerFactory.getLogger(LazyConnectionTestService::class.java)

  @Transactional
  fun withoutDbTask() {
    logConnection()
  }

  @Transactional
  fun withDbTask() {
    logConnection()
    userRepository.save(User.of(UUID.randomUUID().toString()))
    userRepository.flush()
    logConnection()
  }

  private fun logConnection() {
    val hikariPoolMXBean = when (dataSource) {
      is LazyConnectionDataSourceProxy -> (dataSource.targetDataSource as HikariDataSource).hikariPoolMXBean
      else -> (dataSource as HikariDataSource).hikariPoolMXBean
    }

    log.info("hikariPool activeConnection={}", hikariPoolMXBean.activeConnections)
  }
}

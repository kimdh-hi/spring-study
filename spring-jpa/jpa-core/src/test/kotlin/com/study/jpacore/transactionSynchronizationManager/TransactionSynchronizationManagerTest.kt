package com.study.jpacore.transactionSynchronizationManager

import com.study.jpacore.service.UserService
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Commit
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.util.UUID

@SpringBootTest
class TransactionSynchronizationManagerTest @Autowired constructor(
  private val userService: UserService,
) {

  private val log = LoggerFactory.getLogger(TransactionSynchronizationManager::class.java)

  @Test
  @Transactional
  @Commit
  fun afterCommit() {
    userService.save(UUID.randomUUID().toString())
  }
}

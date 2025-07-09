package com.study.jpacore.service

import com.study.jpacore.entity.User
import com.study.jpacore.repository.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * 6.6.x 예외 발생
 *
 * detached entity save 시 insert 쿼리가 발생되는 것은 데이터 무결성 위배 가능하므로 예외로 처리
 * - https://docs.jboss.org/hibernate/orm/6.6/migration-guide/migration-guide.html#merge-versioned-deleted
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class IdAssignedEntitySaveErrorTest @Autowired constructor(
  private val userRepository: UserRepository,
) {

  private val log = LoggerFactory.getLogger(IdAssignedEntitySaveErrorTest::class.java)

  @Test
  fun `id 할당 후 save시 error 발생`() {
    val user = User.of("name").apply { this.id = UUID.randomUUID().toString() }
    assertThrows<OptimisticLockingFailureException> { userRepository.save(user) }
  }

  @Test
  fun `detached entity save 시 error 발생`() {
    val user = User.of("name")
    val savedUser = userRepository.save(user)
    log.info("user == saveUser: {}", user == savedUser)
    userRepository.delete(savedUser)

    assertThrows<InvalidDataAccessApiUsageException> { userRepository.save(savedUser) }
  }
}

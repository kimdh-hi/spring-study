package com.toy.springboot3.repository

import com.toy.springboot3.domain.User
import com.toy.springboot3.domain.UserType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.OptimisticLockingFailureException
import java.util.UUID

/**
 * hibernate 6.6.x detached entity save 예외
 *
 * detached entity save 시 insert 발생은 데이터 무결성 위배 가능하므로 예외로 처리
 * - https://docs.jboss.org/hibernate/orm/6.6/migration-guide/migration-guide.html#merge-versioned-deleted
 */
@SpringBootTest
class IdAssignedEntitySaveErrorTest @Autowired constructor(
  private val userRepository: UserRepository,
) {

  @Test
  fun `id 할당 후 save 시 예외 발생`() {
    val user = User(id = UUID.randomUUID().toString(), name = "name", userType = UserType.USER)
    assertThrows<OptimisticLockingFailureException> { userRepository.save(user) }
  }

  @Test
  fun `삭제된 detached entity save 시 예외 발생`() {
    val savedUser = userRepository.save(User(name = "name", userType = UserType.USER))
    userRepository.delete(savedUser)

    assertThrows<OptimisticLockingFailureException> { userRepository.save(savedUser) }
  }
}

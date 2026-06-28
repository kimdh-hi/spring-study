package com.study.jpacore.`except-case-test`

import com.study.jpacore.entity.Friend
import com.study.jpacore.entity.User
import com.study.jpacore.entity.UserData
import com.study.jpacore.repository.FriendRepository
import com.study.jpacore.repository.UserDataRepository
import com.study.jpacore.repository.UserRepository
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class TransientObjectExceptionTest @Autowired constructor(
  private val userRepository: UserRepository,
  private val friendRepository: FriendRepository,
  private val userDataRepository: UserDataRepository,
  private val entityManager: EntityManager,
) {

  @Test
  fun test1() {
    val fromUser = userRepository.save(User.of("fromUser"))
    val toUser = userRepository.save(User.of("toUser"))
    val friend1 = friendRepository.save(Friend.of(fromUser, toUser))
    entityManager.flush()
    entityManager.clear()

    userRepository.deleteById(fromUser.id!!)

    entityManager.flush()
  }

  @Test
  fun test2() {
    val user = userRepository.save(User.of("user"))
    userDataRepository.save(UserData(data = "data", user = user))
    entityManager.flush()
    entityManager.clear()

    userRepository.deleteById(user.id!!)

    entityManager.flush()
  }
}

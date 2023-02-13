package com.toy.jpaeventlistener.listener

import com.toy.jpaeventlistener.domain.User
import com.toy.jpaeventlistener.domain.UserParticipant
import com.toy.jpaeventlistener.domain.UserParticipantRepository
import com.toy.jpaeventlistener.domain.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserJpaListenerTest(
  private val userRepository: UserRepository,
  private val userParticipantRepository: UserParticipantRepository
) {

  @Test
  fun test() {
    //given
    val user = userRepository.save(User(username = "username"))
    val userParticipant = userParticipantRepository.save(UserParticipant.of(user.id!!))

    //when
    userRepository.delete(user)
  }
}
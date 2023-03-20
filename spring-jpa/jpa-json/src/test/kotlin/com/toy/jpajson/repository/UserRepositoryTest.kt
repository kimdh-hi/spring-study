package com.toy.jpajson.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.jpajson.domain.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class UserRepositoryTest @Autowired constructor(
  private val userRepository: UserRepository,
  private val objectMapper: ObjectMapper
) {

  @Test
  fun test() {
    //given
    val avatar = Avatar(hair = "hair-path", body = "body-path")
    val avatarJsonMap = objectMapper.convertValue(avatar, Map::class.java) as Map<String, Any>
    val avatarJson = objectMapper.writeValueAsString(avatar)
    val user = User(name = "name", avatarV1 = avatarJsonMap, avatarV2 = avatarJson)

    val savedUser = userRepository.save(user)

    //when
    val findUser = userRepository.findByIdOrNull(savedUser.id!!)!!

    //then
    println(findUser)
  }
}

data class Avatar(
  val hair: String,
  val body: String,
)
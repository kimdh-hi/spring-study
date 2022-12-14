package com.toy.springgraphql.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.springgraphql.base.toMap
import com.toy.springgraphql.vo.UserSaveRequestVO
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.GraphQlTester
import org.springframework.test.context.TestConstructor

@SpringBootTest
@AutoConfigureGraphQlTester
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserServiceTest(
  private val graphQlTester: GraphQlTester,
  private val objetMapper: ObjectMapper
) {

  @Test
  fun `findUser`() {
    graphQlTester.documentName("userFindUser")
      .variable("id", "u1")
      .execute()
      .path("findUser.id").entity(String::class.java).isEqualTo("u1")
      .path("findUser.name").entity(String::class.java).isEqualTo("u1-name")
      .path("findUser.username").entity(String::class.java).isEqualTo("u1-username")
  }

  @Test
  fun `findUsers`() {
    graphQlTester.documentName("userFindUsers")
      .execute()
      .path("findUsers[*].id").entityList(String::class.java).hasSizeGreaterThan(1)
  }

  @Test
  fun saveV2() {
    val name = "save-v2-name"
    val username = "save-v2-username"
    val requestVO = UserSaveRequestVO(name = name, username = username)

    graphQlTester.documentName("UserSaveV2")
      .variable("userSaveRequest", toMap(requestVO))
      .execute()
      .path("saveUserV2.name").entity(String::class.java).isEqualTo(name)
      .path("saveUserV2.username").entity(String::class.java).isEqualTo(username)
  }

}
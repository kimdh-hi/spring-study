package com.toy.springgraphql.service

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

}
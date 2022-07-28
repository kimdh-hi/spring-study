package com.toy.reactivejdsl.controller

import com.toy.reactivejdsl.base.BaseTest
import com.toy.reactivejdsl.base.TestData
import com.toy.reactivejdsl.security.JwtUtil
import com.toy.reactivejdsl.vo.UserSaveRequestVO
import com.toy.reactivejdsl.vo.UserSearchVO
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.blockhound.BlockHound

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "100000")
internal class UserControllerTest(
  val webClient: WebTestClient,
  val jwtUtil: JwtUtil
): BaseTest() {

  @Test
  fun `get`() {
    webClient.get()
      .uri("/api/users/${TestData.USER.id}")
      .header(HttpHeaders.AUTHORIZATION, jwtUtil.createToken(TestData.USER))
      .exchange()
      .expectStatus().isOk
  }

  @Test
  fun `getV2 - commonRepo`() {
    webClient.get()
      .uri("/api/users/v2/${TestData.USER.id}")
      .header(HttpHeaders.AUTHORIZATION, jwtUtil.createToken(TestData.USER))
      .exchange()
      .expectStatus().isOk
  }

  @Test
  fun `save`(){
    //given
    val requestVO = UserSaveRequestVO(
      name = "saveName", username = "saveUsername@gmail.com", "pass1234", "role-09", "comp-01")
    val token = jwtUtil.createToken(TestData.USER)

    webClient.post()
      .uri("/api/users")
      .header(HttpHeaders.AUTHORIZATION, token)
      .bodyValue(requestVO)
      .exchange()
      .expectStatus().isOk
  }

  @Test
  fun `search`() {
    //given
    val pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name"))
    val searchVO = UserSearchVO(roleId = "role-09", keyword = "kim")
    val token = jwtUtil.createToken(TestData.USER)

    //when
    webClient.get()
      .uri {
        it
          .path("/api/users")
          .queryParams(convertVO(searchVO))
          .queryParams(convertPageable(pageable))
          .build()
      }
      .header(HttpHeaders.AUTHORIZATION, token)
      .exchange()
      .expectStatus().isOk
  }
}
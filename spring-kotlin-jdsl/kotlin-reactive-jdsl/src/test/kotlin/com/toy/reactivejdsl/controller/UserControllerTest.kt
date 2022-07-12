package com.toy.reactivejdsl.controller

import com.toy.reactivejdsl.BaseTest
import com.toy.reactivejdsl.vo.UserSaveRequestVO
import com.toy.reactivejdsl.vo.UserSearchVO
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "100000")
internal class UserControllerTest(
  val webClient: WebTestClient
): BaseTest() {

  @Test
  fun `save`(){
    //given
    val requestVO = UserSaveRequestVO(
      name = "saveName", username = "saveUsername@gmail.com", "pass1234", "role-09", "comp-01")

    webClient.post()
      .uri("/api/users")
      .bodyValue(requestVO)
      .exchange()
      .expectStatus().isOk
  }

  @Test
  fun `search`() {
    //given
    val pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name"))
    val searchVO = UserSearchVO(roleId = "role-09", keyword = "kim")

    //when
    webClient.get()
      .uri {
        it
          .path("/api/users")
          .queryParams(convertVO(searchVO))
          .queryParams(convertPageable(pageable))
          .build()
      }
      .exchange()
      .expectStatus().isOk
  }
}
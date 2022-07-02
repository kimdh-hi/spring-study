package com.toy.jpahierarchy.controller

import com.toy.jpahierarchy.base.TestData
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class PartnerControllerTest(val mockMvc: MockMvc) {

  @Test
  fun `readV2` () {
    //when
    mockMvc.get("/api/partners/v2/${TestData.P_1_ID}")

  }

  @Test
  fun `listV1` () {
    //when
    mockMvc.get("/api/partners")
  }
}
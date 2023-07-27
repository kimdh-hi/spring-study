package com.toy.springgeoip

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class TimezoneControllerTest @Autowired constructor(
  private val mockMvc: MockMvc
) {
  @Test
  fun getTimezones() {
    mockMvc.get("/timezone")
      .andDo { print() }
  }

  @Test
  fun getAutoTimezone() {
    mockMvc.post("/timezone/auto")
      .andDo { print() }
  }
}
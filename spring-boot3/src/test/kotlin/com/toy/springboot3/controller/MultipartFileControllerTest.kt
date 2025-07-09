package com.toy.springboot3.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.multipart

@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.SYSTEM_OUT, printOnlyOnFailure = false)
class MultipartFileControllerTest @Autowired constructor(
  private val mockMvc: MockMvc,
) {

  @Test
  fun post() {
    val result = mockMvc.multipart("/multipart-files") {
      part()
      file(MockMultipartFile("file", "filename.txt", "text/plain", "test-content".toByteArray()))
      param("data1", "data1")
      param("data2", "data2")
      param("data3", "data3")
      param("data4", "data4")
      param("data5", "data5")
      param("data6", "data6")
      param("data7", "data7")
      param("data8", "data8")
      param("data9", "data9")
    }

    result.andExpect {
      status { isOk() }
    }
  }

}

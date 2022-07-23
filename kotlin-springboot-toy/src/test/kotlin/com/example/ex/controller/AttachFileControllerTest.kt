package com.example.ex.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.multipart

@AutoConfigureMockMvc
@SpringBootTest
class AttachFileControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper

    @Test
    fun test() {
        val uri = "/api/files"

        val image = MockMultipartFile("files", "image.png", "image/png", "image".byteInputStream())

        mockMvc.multipart(uri) {
            file(image)
            param("fileName", "fileName")

        }
            .andDo { print() }
            .andExpect { status { isOk() } }
    }
}
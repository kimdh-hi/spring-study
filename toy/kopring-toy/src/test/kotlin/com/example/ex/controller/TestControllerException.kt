package com.example.ex.controller

import com.example.ex.dto.request.TestDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
@WebMvcTest(controllers = [ExceptionController::class])
class TestControllerException {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var om: ObjectMapper

    @Test
    fun test() {
        val testDto = TestDto()
        val testDtoJson = om.writeValueAsString(testDto)
        mockMvc.post("/validation/test") {
            contentType = MediaType.APPLICATION_JSON
            content = testDtoJson
        }
            .andDo { print() }
    }
}
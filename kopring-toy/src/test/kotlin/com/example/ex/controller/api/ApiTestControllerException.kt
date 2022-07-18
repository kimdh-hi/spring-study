package com.example.ex.controller.api

import com.example.ex.controller.api.ApiTestController.MemberVO
import com.example.ex.controller.vo.RequestVO
import com.example.ex.domain.Member
import com.example.ex.helper.MultiValueMapConverter
import com.example.ex.repository.MemberRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.time.LocalDateTime

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
internal class ApiTestControllerException(
    val mockMvc: MockMvc,
    val memberRepository: MemberRepository,
    val objectMapper: ObjectMapper
) {

    @Test
    fun test() {
        mockMvc.post("/api/vo") {
            contentType = MediaType.APPLICATION_JSON
            content = "{\"data\": null}"
        }.andDo { print() }
    }

    @Test
    fun test1() {

        for (i in 0..10){
            memberRepository.save(Member(username = "kim", date = LocalDateTime.now()))
        }

        val memberVO = objectMapper.writeValueAsString(MemberVO("kim"))
        mockMvc.get("/api/members") {
            param("page", "0")
            param("size", "10")
            param("sort", "date")
            content = memberVO
            contentType = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }
    }
}
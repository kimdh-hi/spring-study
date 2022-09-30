package com.study.mapstruct.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.study.mapstruct.domain.User
import com.study.mapstruct.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
internal class UserControllerTest(
    val userRepository: UserRepository,
    val objectMapper: ObjectMapper,
    val mockMvc: MockMvc
) {

    @Test
    fun test() {
        val savedUser = userRepository.save(User.newInstance("kim@gmail.com", "kim"))
        mockMvc.get("/users/{id}", savedUser.id)
            .andDo {
                print()
            }.andExpect {
                status { isOk() }
            }
    }
}
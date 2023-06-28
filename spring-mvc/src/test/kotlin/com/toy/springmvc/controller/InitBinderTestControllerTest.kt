package com.toy.springmvc.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

class InitBinderTestControllerTest: AbstractBaseController() {

    @Test
    fun test() {
        //given
        val vo = InitBinderTestVO(data = "data")

        //when
        val result = mockMvc.post("/init-binder") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(vo)
        }
                .andDo { print() }

        //then
        result.andExpect {
            status { isOk() }
        }
    }
}

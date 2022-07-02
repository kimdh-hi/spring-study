package com.example.ex.exception

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
class ExceptionControllerTest(val mockMvc: MockMvc) {

    @Test
    fun test1() {
        mockMvc.get("/api/exception")
            .andDo {
                print()
            }
    }

    @Test
    fun test2() {
        mockMvc.get("/view/exception")
            .andDo {
                print()
            }
    }
}
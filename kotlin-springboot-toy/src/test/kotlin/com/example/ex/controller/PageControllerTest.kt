package com.example.ex.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SpringBootTest
@AutoConfigureMockMvc
class PageControllerTest {

    @Autowired lateinit var mockMvc: MockMvc

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

    @Test
    fun test() {
        val url = "/list"

        mockMvc.get(url) {
            param("page", "1")
            param("size", "10")
            param("sortBy", "title")
            param("isAsc", "true")
            param("searchType", "title")
            param("query", "hello")
            param("startDate", LocalDateTime.of(2022, 1, 1,1,1,1).format(formatter))
            param("endDate", LocalDateTime.of(2022, 2, 1,1,1,1).format(formatter))
        }.andDo {
            print()
        }
    }


    @Test
    fun test2() {
        val url = "/list"

        mockMvc.get(url) {
            param("page", "1")
            param("size", "10")
            param("query", "hello")
            param("startDate", LocalDateTime.of(2022, 1, 1,1,1,1).format(formatter))
            param("endDate", LocalDateTime.of(2022, 2, 1,1,1,1).format(formatter))
        }.andDo {
            print()
        }
    }

    @Test
    fun test3() {
        val url = "/date"
        val date = LocalDate.of(2022, 1, 1).format(formatter)

        mockMvc.get(url) {
            param("date", date)
        }.andDo {
            print()
        }
    }
}
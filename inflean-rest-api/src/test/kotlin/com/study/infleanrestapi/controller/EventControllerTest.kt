package com.study.infleanrestapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.study.infleanrestapi.config.MapperConfig
import com.study.infleanrestapi.helper.EventTestHelper
import com.study.infleanrestapi.repository.EventRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
internal class EventControllerTest(
    val eventRepository: EventRepository,
    val mockMvc: MockMvc, val objectMapper: ObjectMapper) {

    @Test
    fun `이벤트 생성` () {
        //given
        val eventVO = EventTestHelper.createEventVO()

        //when
        val result = mockMvc.post("/api/events") {
            content = objectMapper.writeValueAsString(eventVO)
            contentType = MediaType.APPLICATION_JSON
            accept = MediaTypes.HAL_JSON
        }.andDo {
            print()
        }

        //then
        result.andExpect {
            status { isCreated() }
            jsonPath("$.id") { exists() }
            jsonPath("$._links.self.href") { exists() }
            jsonPath("$._links.events") { exists() }
            jsonPath("$._links.update-event") { exists() }
            header { exists("Location") }
        }

        assertAll({
            assertEquals(1, eventRepository.count())
        })
    }

    @Test
    fun `이벤트 생성 - 불필요한 데이터 전달` () {
        //given
        val event = EventTestHelper.createEvent()

        //when
        val result = mockMvc.post("/api/events") {
            content = objectMapper.writeValueAsString(event)
            contentType = MediaType.APPLICATION_JSON
            accept = MediaTypes.HAL_JSON
        }.andDo {
            print()
        }

        //then
        result.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `이벤트 생성 - validation 위반` () {
        //given
        val event = EventTestHelper.createEventVO(name = " ", description = "")

        //when
        val result = mockMvc.post("/api/events") {
            content = objectMapper.writeValueAsString(event)
            contentType = MediaType.APPLICATION_JSON
            accept = MediaTypes.HAL_JSON
        }.andDo {
            print()
        }

        //then
        result.andExpect {
            status { isBadRequest() }
        }
    }


    @Test
    fun `이벤트 생성 - custom validation 위반` () {
        //given
        val event = EventTestHelper.createEventVO(basePrice = 1000, maxPrice = 100)

        //when
        val result = mockMvc.post("/api/events") {
            content = objectMapper.writeValueAsString(event)
            contentType = MediaType.APPLICATION_JSON
            accept = MediaTypes.HAL_JSON
        }.andDo {
            print()
        }

        //then
        result.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `이벤트 생성 - validation body 응답` () {
        //given
        val event = EventTestHelper.createEventVO(name = "")

        //when
        val result = mockMvc.post("/api/events") {
            content = objectMapper.writeValueAsString(event)
            contentType = MediaType.APPLICATION_JSON
            accept = MediaTypes.HAL_JSON
        }.andDo {2
            print()
        }

        //then
        result.andExpect {
            status { isBadRequest() }
            jsonPath("$[0].field") { exists() }
            jsonPath("$[0].objectName") { exists() }
            jsonPath("$[0].message") { exists() }
            jsonPath("$[0].code") { exists() }
        }
    }
}
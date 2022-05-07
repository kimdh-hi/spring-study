package com.study.infleanrestapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.study.infleanrestapi.helper.EventTestHelper
import com.study.infleanrestapi.repository.EventRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.hateoas.MediaTypes
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@WebMvcTest(controllers = [EventController::class])
internal class EventControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper) {

    @MockkBean lateinit var eventRepository: EventRepository

    @Test
    fun `이벤트 생성` () {
        //given
        val event = EventTestHelper.createEvent()
        event.id = 10

        //when-then
        every { eventRepository.save(any())} returns(event)



        mockMvc.post("/api/events/") {
            content = objectMapper.writeValueAsString(event)
            contentType = MediaType.APPLICATION_JSON
            accept = MediaTypes.HAL_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isCreated() }
            jsonPath("$.id") {
                exists()
                value(10)
            }
            header { exists("Location") }
        }
    }
}
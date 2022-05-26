package com.example.ex.controller.api

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@SpringBootTest
internal class LocalizationApiControllerTest(val mockMvc: MockMvc) {

    @Test
    fun `locale-default`() {
        val andReturn = mockMvc.get("/api/localization") {
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
        }.andReturn()

        assertEquals("안녕", andReturn.response.contentAsString)
    }

    @Test
    fun `locale-en`() {
        val andReturn = mockMvc.get("/api/localization") {
            param("locale", "en")
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
        }.andReturn()

        assertEquals("hello", andReturn.response.contentAsString)
    }

    @Test
    fun `locale-jp`() {
        val andReturn = mockMvc.get("/api/localization") {
            param("locale", "jp")
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
        }.andReturn()

        assertEquals("こんにちは", andReturn.response.contentAsString)
    }

    @Test
    fun `locale-zh_CN`() {
        val andReturn = mockMvc.get("/api/localization") {
            param("locale", "zh_CN")
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
        }.andReturn()

        assertEquals("你好。(CN)", andReturn.response.contentAsString)
    }

    @Test
    fun `locale-zh_TW`() {
        val andReturn = mockMvc.get("/api/localization") {
            param("locale", "zh_TW")
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
        }.andReturn()

        assertEquals("你好。(TW)", andReturn.response.contentAsString)
    }

    @Test
    fun `locale-en-params`() {
        val andReturn = mockMvc.get("/api/localization/params") {
            param("locale", "en")
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
        }.andReturn()

        assertEquals("my name is 김대현", andReturn.response.contentAsString)
    }

    @Test
    fun `locale-en-params-newline`() {
        val andReturn = mockMvc.get("/api/localization/params-newline") {
            param("locale", "en")
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
        }.andReturn()

    }

    @Test
    fun `accept-header` () {
        mockMvc.get("/api/localization/header")
            .andDo {
                print()
            }
    }
}
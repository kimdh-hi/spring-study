package com.study.jasypt.config

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class CustomPropertiesTest(val customProperties: CustomProperties) {

    val LOG = LoggerFactory.getLogger(javaClass)

    @Test
    fun `property decryption test`() {
        //given
        val encValue = customProperties.encValue

        //expect
        LOG.info("encValue: {}", encValue)
        assertEquals("enc-value", encValue)
    }

    @Test
    fun `customProperties test`() {
        //given
        val testValue = customProperties.plainValue

        //expect
        LOG.info("testValue: {} ", testValue)
        assertEquals("plain-value", testValue)
    }
}
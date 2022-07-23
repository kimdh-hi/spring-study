package com.example.ex.test

import org.apache.commons.lang.StringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID

class KotlinTest {

    @Test
    fun test() {
        val time = LocalDateTime.now().plusDays(91)

        if(time.isBefore(LocalDateTime.now().plusDays(90))) {
            println("isBefore ok")
        } else {
            println("isBefore no")
        }
    }

    @Test
    fun test1() {
        val ids = mutableListOf<String>()
        ids.add(UUID.randomUUID().toString())
        ids.add(UUID.randomUUID().toString())


        val idsStr = ids.joinToString(",")

        println(idsStr)
    }

    @Test
    fun test2() {
        assertThrows<RuntimeException> {
            ResourceVO.newInstance("aaa")
        }

        assertDoesNotThrow {
            ResourceVO.newInstance("ko")
        }
    }

    data class ResourceVO(val language: String) {

        init {
            checkLanguage()
        }

        companion object {
            fun newInstance(language: String): ResourceVO {
                return ResourceVO(language = language)
            }

            private val LANGUAGES = listOf("ko", "en", "jp")
        }

        private fun checkLanguage() {
            val exists = LANGUAGES.any {
                StringUtils.equals(it, language)
            }

            if (!exists)
                throw RuntimeException("$language is not supported language...")
        }
    }
}
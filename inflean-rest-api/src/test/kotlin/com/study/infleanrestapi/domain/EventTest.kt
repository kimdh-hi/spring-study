package com.study.infleanrestapi.domain

import com.study.infleanrestapi.helper.EventTestHelper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.test.context.TestConstructor

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class EventTest {

    @ParameterizedTest
    @MethodSource("parametersForFreeTest")
    fun `free`(basePrice: Int, maxPrice:Int, free: Boolean) {
        //given-when
        val event = EventTestHelper.createEvent(basePrice = basePrice, maxPrice = maxPrice)

        //then
        assertEquals(free, event.free)
    }

    @ParameterizedTest
    @MethodSource("parametersForOfflineTest")
    fun `offline`(location: String?, offline: Boolean) {
        // given-when
        val event = EventTestHelper.createEvent(location = location)

        //then
        assertEquals(offline, event.offline)
    }

    private fun parametersForFreeTest(): List<Arguments> {
        return listOf<Arguments>(
            Arguments.of(1000, 0, false), // basePrice 만 있는 경우
            Arguments.of(0, 1000, false), // maxPrice 만 있는 경우
            Arguments.of(0, 0, true), // 모두 없는 경우
        )
    }

    private fun parametersForOfflineTest(): List<Arguments> {
        return listOf<Arguments>(
            Arguments.of("seoul", false),
            Arguments.of(null, true)
        )
    }
}
package com.example.ex.test

import org.junit.jupiter.api.Test

class KotlinTest2 {

//    @Test
//    fun test() {
//        TestVO(data1 = null, data2 = null ) // compile error
//    }

    @Test
    fun test2() {
        val testVO = TestVO(data1 = null, data2 = "")
        println("length: ${testVO.data1?.length ?: 0}")
    }

    data class TestVO (
        val data1: String?,
        val data2: String
    )
}
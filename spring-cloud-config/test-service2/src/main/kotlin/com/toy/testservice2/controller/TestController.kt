package com.toy.testservice2.controller

import com.toy.testservice2.service.TestService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val testService: TestService
) {

    @GetMapping("/api/test")
    fun test(): String? {
        return testService.test()
    }

}
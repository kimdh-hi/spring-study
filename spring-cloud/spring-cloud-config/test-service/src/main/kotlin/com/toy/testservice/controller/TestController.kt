package com.toy.testservice.controller

import com.toy.testservice.service.TestService
import org.springframework.beans.factory.annotation.Value
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
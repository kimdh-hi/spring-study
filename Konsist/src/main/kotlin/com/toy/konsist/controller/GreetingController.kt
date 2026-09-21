package com.toy.konsist.controller

import com.toy.konsist.service.GreetingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingController(
    private val greetingService: GreetingService,
) {
    @GetMapping("/greeting")
    fun greeting(): String = greetingService.greet()
}

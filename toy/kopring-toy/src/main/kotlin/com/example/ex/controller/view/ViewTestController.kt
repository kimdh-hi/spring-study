package com.example.ex.controller.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/view")
@Controller
class ViewTestController {

    @GetMapping("/exception")
    fun test() {
        throw IllegalStateException("ex")
    }
}
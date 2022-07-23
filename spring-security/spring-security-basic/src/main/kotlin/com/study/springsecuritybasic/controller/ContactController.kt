package com.study.springsecuritybasic.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/contacts")
@RestController
class ContactController {

    @GetMapping
    fun contacts() = "contacts"
}
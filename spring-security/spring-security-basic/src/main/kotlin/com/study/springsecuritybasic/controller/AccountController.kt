package com.study.springsecuritybasic.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/accounts")
@RestController
class AccountController {

    @GetMapping
    fun accounts() = "accounts"
}
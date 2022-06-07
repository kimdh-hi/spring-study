package com.toy.springjavamail.controller

import com.toy.springjavamail.service.MailService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/mail")
@RestController
class MailController(
    private val mailService: MailService
) {

    @PostMapping
    fun send(): ResponseEntity<String> {
        mailService.send("dhkim2@rsupport.com", "testTemplate")

        return ResponseEntity.ok("ok")
    }
}
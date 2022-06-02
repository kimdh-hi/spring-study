package com.toy.springjavamail.controller

import com.toy.springjavamail.common.MailSender
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/mail")
@RestController
class MailController(private val mailSender: MailSender) {

    @PostMapping
    fun send(@RequestParam to: String, @RequestParam subject: String, @RequestParam message: String): ResponseEntity<String> {
        mailSender.send(to, subject, message)

        return ResponseEntity.ok("ok")
    }
}
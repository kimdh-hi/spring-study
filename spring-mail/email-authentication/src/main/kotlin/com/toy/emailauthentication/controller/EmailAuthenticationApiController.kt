package com.toy.emailauthentication.controller

import com.toy.emailauthentication.service.EmailAuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/email")
class EmailAuthenticationApiController(private val emailAuthenticationService: EmailAuthenticationService) {

  @GetMapping("/verify/{id}")
  fun verify(@PathVariable id: String): ResponseEntity<Unit> {
    emailAuthenticationService.authenticate(id)

    return ResponseEntity.ok().build()
  }
}
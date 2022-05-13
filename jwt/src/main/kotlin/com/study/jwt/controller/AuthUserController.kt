package com.study.jwt.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@PreAuthorize("hasAuthority(T(com.study.jwt.domain.Authority).USER)")
@RequestMapping("/api/user")
@RestController
class AuthUserController {

    @GetMapping("/hello")
    fun hello() = ResponseEntity.ok("hello")
}
package com.study.jwt.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority(T(com.study.jwt.domain.Authority).ADMIN)")
@RestController
class AuthAdminController {

    @GetMapping("/hello")
    fun hello() = ResponseEntity.ok("hello")
}
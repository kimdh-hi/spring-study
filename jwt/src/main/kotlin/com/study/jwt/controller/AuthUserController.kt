package com.study.jwt.controller

import com.study.jwt.util.SecurityUtil
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@PreAuthorize("hasAuthority(T(com.study.jwt.domain.Authority).USER)")
@RequestMapping("/api/user")
@RestController
class AuthUserController {

    val LOG = LoggerFactory.getLogger(javaClass)

    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> {
        val currentPrincipal = SecurityUtil.getCurrentPrincipal()
        LOG.info("auth-user principal: {}", currentPrincipal)

        return ResponseEntity.ok("hello")
    }
}
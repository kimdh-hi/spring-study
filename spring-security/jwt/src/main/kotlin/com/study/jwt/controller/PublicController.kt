package com.study.jwt.controller

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/public")
@RestController
class PublicController {

    val LOG = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun hello(): ResponseEntity<String> {
        LOG.info("public-api")
        return ResponseEntity.ok("public")
    }
}
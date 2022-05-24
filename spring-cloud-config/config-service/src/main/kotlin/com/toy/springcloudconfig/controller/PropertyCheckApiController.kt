package com.toy.springcloudconfig.controller

import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/property")
@RestController
class PropertyCheckApiController(private val env: Environment) {

    @GetMapping
    fun propertyCheck(): ResponseEntity<String> {
        val secret = env.getProperty("my.secret")

        return ResponseEntity.ok(secret)
    }
}
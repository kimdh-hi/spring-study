package com.study.swagger.controller

import com.study.swagger.vo.RequestVO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "tag")
@RestController
class TestController {

    @Operation(summary = "summary", method = "method", description = "description")
    @GetMapping("/api/test")
    fun test(@RequestBody requestVO: RequestVO): ResponseEntity<String> {

        return ResponseEntity.ok("ok")
    }
}
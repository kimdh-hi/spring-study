package com.example.ex.controller.api

import com.example.ex.controller.vo.ResponseVO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RequestMapping("/timezone")
@RestController
class TimeZoneApiController {

    @GetMapping("/test")
    fun test() = ResponseEntity.ok(ResponseVO(LocalDateTime.now()))
}
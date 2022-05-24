package com.example.ex.controller.api

import com.example.ex.common.MessageSourceService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RequestMapping("/api/localization")
@RestController
class LocalizationApiController(private val messageSourceService: MessageSourceService) {

    private val LOG = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun localization(request: HttpServletRequest): ResponseEntity<String> {
        val message = messageSourceService.getMessage("key1")
        return ResponseEntity.ok(message)
    }

    @GetMapping("/params")
    fun localizationParams(): ResponseEntity<String> {
        val message = messageSourceService.getMessage("key3", "김대현")

        return ResponseEntity.ok(message)
    }

    @GetMapping("/params-newline")
    fun localizationParamsNewline(): ResponseEntity<String> {
        val message = messageSourceService.getMessage("key4", "김대현")

        return ResponseEntity.ok(message)
    }

}
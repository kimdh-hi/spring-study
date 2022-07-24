package com.example.ex.controller

import com.example.ex.dto.request.DateRequest
import com.example.ex.dto.request.SearchCondition
import com.example.ex.dto.request.search.SearchRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
class PageController {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/list")
    fun list(pageable: Pageable, @ModelAttribute searchRequest: SearchRequest) : SearchRequest {
        log.info("pageable.pageNumber = {}", pageable.pageNumber)
        log.info("pageable.pageSize = {}", pageable.pageSize)
        val sort = pageable.sort
        sort.forEach {
            log.info("sort-property={}, direction={}", it.property, it.direction)
        }
        log.info("request = {}", searchRequest)
        return searchRequest
    }

    @GetMapping("/date")
    fun date(dateRequest: DateRequest) = dateRequest
}
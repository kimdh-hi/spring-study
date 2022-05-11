package com.study.swagger.controller

import com.study.swagger.vo.RequestVO
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PostApiController {

    private val LOG = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "Summary", description = "Description")
    @GetMapping("/api/posts/{id}")
    fun read(@ApiParam(value = "id") @PathVariable id: Long): ResponseEntity<Long> {
        LOG.info("read id: {}", id)
        return ResponseEntity.ok(id)
    }

    @Operation(summary = "게시글 저장")
    @PostMapping("/api/posts")
    fun save(@RequestBody requestVO: RequestVO): ResponseEntity<RequestVO> {
        LOG.info("save requestVO: {}", requestVO)
        return ResponseEntity.ok(requestVO)
    }
}
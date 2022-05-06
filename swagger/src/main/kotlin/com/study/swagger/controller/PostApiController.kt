package com.study.swagger.controller

import com.study.swagger.vo.RequestVO
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PostApiController {

    @Operation(summary = "Summary", description = "Description")
    @ApiResponses(
        value = [ApiResponse(code = 200, message = "ok")])
    @GetMapping("/api/posts/{id}")
    fun read(@ApiParam(value = "id") @PathVariable id: Long): ResponseEntity<Long> {
        return ResponseEntity.ok(id)
    }

    @ApiResponses(
        value = [ApiResponse(code = 200, message = "ok")])
    @PostMapping("/api/posts")
    fun save(@RequestBody requestVO: RequestVO): ResponseEntity<RequestVO> {

        return ResponseEntity.ok(requestVO)
    }
}
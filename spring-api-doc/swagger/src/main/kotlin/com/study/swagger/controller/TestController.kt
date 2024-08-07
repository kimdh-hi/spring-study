package com.study.swagger.controller

import com.study.swagger.vo.InternalVO
import com.study.swagger.vo.Request2VO
import com.study.swagger.vo.RequestVO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.models.media.Content
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.swing.text.AbstractDocument

@Tag(name = "tag")
@RestController
class TestController {

  @Operation(summary = "summary", method = "method", description = "description")
  @PostMapping("/api/test")
  fun test1(@RequestBody requestVO: RequestVO): ResponseEntity<String> {

    return ResponseEntity.ok("ok")
  }

  @Operation(summary = "summary", method = "method", description = "description")
  @PostMapping("/api/test/test2")
  fun test1(@RequestBody requestVO: RequestVO, a: Int): ResponseEntity<String> {

    return ResponseEntity.ok("ok")
  }

  @PostMapping("/api/test2")
  @ApiResponse(content = [io.swagger.v3.oas.annotations.media.Content(schema = Schema(implementation = InternalVO::class))])
  fun test2(@RequestBody requestVO: Request2VO): ResponseEntity<String> {

      return ResponseEntity.ok("ok")
  }
}
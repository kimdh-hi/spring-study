package com.toy.springswagger_restdocs.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/samples")
@RestController
class SampleController {
  @GetMapping("/{sampleId}")
  fun getSampleById(@PathVariable sampleId: String, ): ResponseEntity<SampleResponse> =
    ResponseEntity.ok(SampleResponse(sampleId, "sample-$sampleId"))
}

data class SampleResponse(
  val sampleId: String,
  val name: String,
)
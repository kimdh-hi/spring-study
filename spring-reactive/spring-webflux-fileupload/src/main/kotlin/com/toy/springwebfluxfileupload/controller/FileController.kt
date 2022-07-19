package com.toy.springwebfluxfileupload.controller

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart

@Controller
@RequestMapping("/file")
class FileController {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping
  fun upload(@RequestPart file: FilePart): ResponseEntity<Unit> {
    log.info("file-name: {}", file.filename())
    return ResponseEntity.ok().build()
  }
}
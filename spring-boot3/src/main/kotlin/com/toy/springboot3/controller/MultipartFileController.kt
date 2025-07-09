package com.toy.springboot3.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/multipart-files")
class MultipartFileController {

  @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
  fun post(request: MultipartFileRequest) {
    return ResponseEntity.ok("ok")
  }
}

data class MultipartFileRequest(
  val file: MultipartFile,
  val data1: String = "data1",
  val data2: String = "data2",
  val data3: String = "data3",
  val data4: String = "data4",
  val data5: String = "data5",
  val data6: String = "data6",
  val data7: String = "data7",
  val data8: String = "data8",
  val data9: String = "data9",
  val data10: String = "data10",
)

package com.toy.springimageresize.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/resize")
class ImageResizeController {

  @GetMapping("/v1")
  fun resizeByGraphics2D() {

  }
}

data class ResizeRequest(
  val width: Int,
  val height: Int,
)
package com.toy.springimageresize.utils

import org.junit.jupiter.api.Test


class ImageResizeUtilsTest {

  @Test
  fun `resize`() {
    val resizedImage = ImageResizeUtils.resize("testImage.png", 400, 400)

  }
}
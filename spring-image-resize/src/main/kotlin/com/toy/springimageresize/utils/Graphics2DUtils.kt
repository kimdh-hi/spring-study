package com.toy.springimageresize.utils

import java.awt.image.BufferedImage

object Graphics2DUtils {

  fun resize(originalImage: BufferedImage, width: Int, height: Int): BufferedImage {
    val resizedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    resizedImage.createGraphics().apply {
      drawImage(originalImage, 0, 0, width, height, null)
      dispose()
    }

    return resizedImage
  }
}
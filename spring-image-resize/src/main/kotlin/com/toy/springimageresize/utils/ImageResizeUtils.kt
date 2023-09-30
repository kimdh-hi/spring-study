package com.toy.springimageresize.utils

import org.springframework.core.io.ClassPathResource
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

object ImageResizeUtils {

  fun resize(path: String, width: Int, height: Int): ByteArrayOutputStream {
    val originalImage = getOriginalImage(path)

    val resizedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    resizedImage.createGraphics().apply {
      drawImage(originalImage, 0, 0, width, height, null)
      dispose()
    }

    val outputStream = ByteArrayOutputStream()
    val extension = path.substringAfterLast(".")
    ImageIO.write(resizedImage, extension, outputStream);

    return outputStream
  }

  private fun getOriginalImage(path: String): BufferedImage {
    val resource = ClassPathResource(path)
    return ImageIO.read(resource.file)
  }
}
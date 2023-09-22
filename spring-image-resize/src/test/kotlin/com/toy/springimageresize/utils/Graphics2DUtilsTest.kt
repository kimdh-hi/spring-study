package com.toy.springimageresize.utils

import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import java.io.File
import javax.imageio.ImageIO


class Graphics2DUtilsTest {

  @Test
  fun `resize`() {
    val resource = ClassPathResource("testImage.png")
    val originalImage = ImageIO.read(resource.file)
    val resizedImage = Graphics2DUtils.resize(originalImage, 400, 400)

    ImageIO.write(resizedImage, "png", File("test-image/resizedImage.png"))
  }
}
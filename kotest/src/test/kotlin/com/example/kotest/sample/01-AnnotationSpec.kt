package com.example.kotest.sample

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class AnnotationSpecTest : AnnotationSpec() {

  @Test
  fun `테스트1`() {
    val data = "data"
    data.shouldBe("data")
  }
}
package com.study.jpacore.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class LazyConnectionTestServiceTest @Autowired constructor(
  private val lazyConnectionTestService: LazyConnectionTestService,
) {

  @Test
  fun withDbTask() {
    val (before, after) = lazyConnectionTestService.withDbTask()

    assertEquals(0, before)
    assertEquals(1, after)
  }

  @Test
  fun withoutDbTask() {
    assertEquals(0, lazyConnectionTestService.withoutDbTask())
  }

}

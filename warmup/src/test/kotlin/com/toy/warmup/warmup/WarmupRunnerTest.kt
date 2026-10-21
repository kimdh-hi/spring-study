package com.toy.warmup.warmup

import org.junit.jupiter.api.Test
import org.springframework.boot.DefaultApplicationArguments
import java.util.concurrent.ExecutionException
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class WarmupRunnerTest {

  @Test
  fun `task failure propagates`() {
    val failing = object : WarmupTask {
      override fun run() = throw IllegalStateException("boom")
    }
    val runner = WarmupRunner(listOf(failing))

    val exception = assertFailsWith<ExecutionException> { runner.run(DefaultApplicationArguments()) }
    assertIs<IllegalStateException>(exception.cause)
  }
}

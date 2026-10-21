package com.toy.warmup.warmup

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.concurrent.Executors
import kotlin.time.TimeSource

@Profile("!no-warmup")
@Component
class WarmupRunner(
  private val tasks: List<WarmupTask>,
) : ApplicationRunner {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun run(args: ApplicationArguments) {
    val started = TimeSource.Monotonic.markNow()

    Executors.newVirtualThreadPerTaskExecutor().use { executor ->
      tasks.forEach { task ->
        executor.execute(task::run)
      }
    }

    log.info("warmup completed. elapsed={}", started.elapsedNow())
  }
}

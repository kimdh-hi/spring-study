package com.toy.warmup.warmup

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.time.measureTime

@Profile("!no-warmup")
@Component
class WarmupRunner(
  private val tasks: List<WarmupTask>,
) : ApplicationRunner {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun run(args: ApplicationArguments) {
    val elapsed = measureTime {
      Executors.newVirtualThreadPerTaskExecutor().use { executor ->
        executor.invokeAll(tasks.map { Callable { execute(it) } }, TIMEOUT_SECONDS, TimeUnit.SECONDS)
      }
    }
    log.info("warmup completed. tasks={}, elapsed={}", tasks.size, elapsed)
  }

  private fun execute(task: WarmupTask) {
    val name = task.javaClass.simpleName
    try {
      val elapsed = measureTime {
        repeat(task.repeat) { i ->
          if (Thread.currentThread().isInterrupted) return
          task.run(i)
        }
      }
      log.info("{} completed. repeat={}, elapsed={}", name, task.repeat, elapsed)
    } catch (e: Exception) {
      log.warn("{} failed", name, e)
    }
  }

  companion object {
    private const val TIMEOUT_SECONDS = 60L
  }
}

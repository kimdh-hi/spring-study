package com.toy.springsse

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.task.TaskExecutor
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
@RequestMapping("/sse")
class SseController(
  private val registry: SseEmitterRegistry,
  @Qualifier("applicationTaskExecutor") private val taskExecutor: TaskExecutor,
) {

  @GetMapping("/subscribe/{clientId}", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
  fun subscribe(
    @PathVariable clientId: String,
    @RequestHeader("Last-Event-ID", required = false) lastEventId: String?,
  ): SseEmitter {
    val emitter = registry.register(clientId)

    registry.send(clientId, "connect", "connected (lastEventId=$lastEventId)")
    return emitter
  }

  @PostMapping("/send/{clientId}")
  fun send(@PathVariable clientId: String, @RequestBody message: String) {
    registry.send(clientId, "message", message)
  }

  @PostMapping("/broadcast")
  fun broadcast(@RequestBody message: String) {
    registry.broadcast("message", message)
  }

  @PostMapping("/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
  fun stream(@RequestBody prompt: String): SseEmitter {
    val emitter = SseEmitter(STREAM_TIMEOUT_MILLIS)

    taskExecutor.execute {
      try {
        generate(prompt).forEach { emitter.send(SseEmitter.event().name("chunk").data(it)) }
        emitter.send(SseEmitter.event().name("done").data("[DONE]"))
        emitter.complete()
      } catch (e: Exception) {
        emitter.completeWithError(e)
      }
    }
    return emitter
  }

  @PostMapping("/stream-flux", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
  fun streamFlux(@RequestBody prompt: String): Flux<ServerSentEvent<String>> =
    Flux.fromIterable(prompt.split(" "))
      .delayElements(Duration.ofMillis(200))
      .map { ServerSentEvent.builder(it).event("chunk").build() }
      .concatWith(Mono.just(ServerSentEvent.builder("[DONE]").event("done").build()))

  private fun generate(prompt: String): Sequence<String> = sequence {
    prompt.split(" ").forEach {
      Thread.sleep(200)
      yield(it)
    }
  }

  companion object {
    private const val STREAM_TIMEOUT_MILLIS = 5 * 60 * 1000L
  }
}

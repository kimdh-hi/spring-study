package com.study.sentry.demo

import io.sentry.Sentry
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/demo")
class DemoController {

  @GetMapping("/ok")
  fun ok() = mapOf("status" to "ok")

  @GetMapping("/error")
  fun error(): Nothing = throw IllegalStateException("unhandled demo error")

  @GetMapping("/message")
  fun message(): Map<String, String> {
    Sentry.captureMessage("manual demo message")
    return mapOf("captured" to "message")
  }

  @GetMapping("/handled")
  fun handled(): Map<String, String> {
    return try {
      throw RuntimeException("handled demo error")
    } catch (e: RuntimeException) {
      Sentry.captureException(e)
      mapOf("captured" to "exception")
    }
  }
}

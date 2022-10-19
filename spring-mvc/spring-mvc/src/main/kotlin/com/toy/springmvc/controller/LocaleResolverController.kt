package com.toy.springmvc.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneId
import java.util.*

@RestController
@RequestMapping("/locale-resolver")
class LocaleResolverController {
  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping
  fun sample(locale: Locale, timeZone: TimeZone, zoneId: ZoneId): String {
    log.info("locale: {}", locale)
    log.info("timeZone: {}", timeZone)
    log.info("zoneId: {}", zoneId)
    return "local-resolver"
  }
}
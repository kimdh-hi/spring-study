package com.toy.springmobile

import org.slf4j.LoggerFactory
import org.springframework.mobile.device.Device
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/test")
class TestController {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping
  fun test(request: HttpServletRequest, device: Device): String {
    val userAgent = request.getHeader("User-Agent")
    val devicePlatform = device.devicePlatform
    val isMobile = device.isMobile
    val isNormal = device.isNormal


//    val device = DeviceUtils.getCurrentDevice(request)
    log.info("""
      userAgent: $userAgent,
      devicePlatform: $devicePlatform,
      isMobile: $isMobile,
      isNormal: $isNormal
    """.trimIndent())

    return "ok"
  }
}
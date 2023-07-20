package com.toy.springmobilestarter

import jakarta.servlet.http.HttpServletRequest
import org.springframework.mobile.device.Device
import org.springframework.mobile.device.DevicePlatform
import org.springframework.mobile.device.DeviceUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

  @GetMapping
  fun test(request: HttpServletRequest, device: Device): DevicePlatform {
    return device.devicePlatform
  }
}
package com.toy.springmvc.controller

import eu.bitwalker.useragentutils.Manufacturer
import eu.bitwalker.useragentutils.OperatingSystem
import eu.bitwalker.useragentutils.UserAgent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/user-agent")
class UserAgentController {

  @GetMapping
  fun get(request: HttpServletRequest): DeviceType {
    val userAgent = UserAgent(request.getHeader("user-agent"))
    return userAgent.getDeviceType()
  }
}

fun UserAgent.getDeviceType(): DeviceType {
  return this.operatingSystem.getDevice()
}

fun OperatingSystem.getDevice(): DeviceType {
  return if (manufacturer == Manufacturer.MICROSOFT || manufacturer == Manufacturer.LINUX_FOUNDATION) {
    DeviceType.WEB
  } else if (manufacturer == Manufacturer.GOOGLE && deviceType == eu.bitwalker.useragentutils.DeviceType.MOBILE) {
    DeviceType.AOS
  } else if (manufacturer == Manufacturer.APPLE && deviceType == eu.bitwalker.useragentutils.DeviceType.MOBILE) {
    DeviceType.IOS
  } else {
    DeviceType.UNKNOWN
  }
}

enum class DeviceType {
  WEB, AOS, IOS, UNKNOWN;
}
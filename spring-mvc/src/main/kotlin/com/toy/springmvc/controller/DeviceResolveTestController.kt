package com.toy.springmvc.controller

import org.slf4j.LoggerFactory
import org.springframework.mobile.device.Device
import org.springframework.mobile.device.DeviceUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/device-resolve")
class DeviceResolveTestController(

) {

  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping
  fun deviceResolve(device: Device, request: HttpServletRequest): Device {
    log.info("deviceResolve device: {}", device)
    log.info("deviceResolve device: {}", DeviceUtils.getCurrentDevice(request))
    return device
  }
}
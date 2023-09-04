package com.toy.springmvc.controller

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
  fun get(request: HttpServletRequest): OperatingSystem {
    val userAgent = UserAgent(request.getHeader("user-agent"))
    return userAgent.operatingSystem
  }
}
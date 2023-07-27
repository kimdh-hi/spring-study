package com.toy.springgeoip

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/timezone")
class TimezoneController(
  private val geoIpUtils: GeoIpUtils,
) {

  @GetMapping
  fun getTimezones(): ResponseEntity<List<TimezoneVO>> {
    val responseVO = TimezoneUtils.getTimezones()
    return ResponseEntity.ok(responseVO)
  }

  @PostMapping("/auto")
  fun getAutoTimezone(request: HttpServletRequest): ResponseEntity<TimezoneVO> {
    val remoteAddr: String = request.remoteAddr
    val timezone = geoIpUtils.getTimezone(remoteAddr, "Asia/Seoul")
    return ResponseEntity.ok(TimezoneVO.of(timezone))
  }
}
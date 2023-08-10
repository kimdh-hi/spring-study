package com.toy.springgeoip

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GeoIpUtilsTest @Autowired constructor(
  private val geoIpUtils: GeoIpUtils
) {

  @Test
  fun test() {
    println(geoIpUtils.getGeoIpLocation("172.0.0.1"))
    println(geoIpUtils.getGeoIpLocation("172.0.0.1"))
  }

  @Test
  fun getTimezone() {
    println(geoIpUtils.getTimezone("172.0.0.1", "Asia/Seoul"))
  }

  @Test
  fun getTimezoneLocal() {
    println(geoIpUtils.getTimezone("127.0.0.1", "Asia/Seoul"))
  }
}
package com.toy.springgeoip

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "geoip")
class GeoIpProperties(
  val path: String
)
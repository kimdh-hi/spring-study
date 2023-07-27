package com.toy.springgeoip

import com.maxmind.db.CHMCache
import com.maxmind.db.Reader
import com.maxmind.geoip2.DatabaseReader
import com.maxmind.geoip2.exception.AddressNotFoundException
import com.maxmind.geoip2.model.CityResponse
import org.slf4j.LoggerFactory
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.net.InetAddress
import java.util.*

@Component
class GeoIpUtils(
  private val geoIpProperties: GeoIpProperties,
  private val resourceLoader: ResourceLoader
) {

  private val log = LoggerFactory.getLogger(javaClass)

  val reader: DatabaseReader by lazy {
    val database = resourceLoader.getResource(geoIpProperties.path).file
    DatabaseReader.Builder(database).fileMode(Reader.FileMode.MEMORY).withCache(CHMCache()).build()
  }

  fun getGeoIpLocation(ip: String): GeoIpLocation {
    return GeoIpLocation.of(findCityResponse(ip))
  }

  fun getTimezone(ip: String, defaultTimezone: String): TimeZone {
    val geoIpLocation = findCityResponse(ip)?.let {
      GeoIpLocation.of(it)
    } ?: GeoIpLocation.of(null, defaultTimezone)

    return TimeZone.getTimeZone(geoIpLocation.timezone)
  }

  private fun findCityResponse(ip: String): CityResponse? {
    return try {
      reader.city(InetAddress.getByName(ip))
    } catch (e: AddressNotFoundException) {
      log.warn("[GeoIp] AddressNotFoundException: ip: {}", ip)
      null
    }
  }
}
package com.toy.springgeoip

import com.maxmind.geoip2.model.CityResponse

private const val DEFAULT_COUNTRY_CODE = "kr"
private const val DEFAULT_TIMEZONE = "Asia/Seoul"


data class GeoIpLocation(
  val city: String? = null,
  val countryCode: String,
  val timezone: String
) {
  companion object {
    fun of(cityResponse: CityResponse?, defaultTimezone: String? = null): GeoIpLocation {
      return cityResponse?.let {
        GeoIpLocation(
          city = it.city.name,
          countryCode = it.country.isoCode,
          timezone = defaultTimezone ?: it.location.timeZone
        )
      } ?: ofDefault()
    }

    private fun ofDefault() = GeoIpLocation(
      countryCode = DEFAULT_COUNTRY_CODE,
      timezone = DEFAULT_TIMEZONE
    )
  }
}
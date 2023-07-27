package com.toy.springgeoip

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.zone.ZoneRulesException
import java.util.*

private const val AVAILABLE_TIMEZONE_REGEX = "^(Africa|America|Asia|Atlantic|Australia|Europe)/.*"

object TimezoneUtils {

  fun adjustTimezone(date: LocalDateTime, zoneId: ZoneId): LocalDateTime {
    val zonedDateTime = ZonedDateTime.of(date, ZoneOffset.UTC)
    return zonedDateTime.withZoneSameInstant(zoneId).toLocalDateTime()
  }

  fun getZoneId(zoneId: String) {
    try {
      ZoneId.of(zoneId)
    } catch (e: ZoneRulesException) {
      // 존재하지 않는 zoneId 의 경우 ZoneRulesException 발생
      println("invalid zoneId...")
      throw IllegalArgumentException("invalid zoneId...")
    }
  }

  fun getTimezones(): List<TimezoneVO> {
    val timezoneVOS = mutableListOf<TimezoneVO>()
    TimeZone.getAvailableIDs().map { timeZoneId ->
      val timeZone = TimeZone.getTimeZone(timeZoneId)
      if (timeZoneId.matches(Regex(AVAILABLE_TIMEZONE_REGEX))) {
        timezoneVOS.add(TimezoneVO.of(timeZone))
      }
    }

    return timezoneVOS.toList()
  }
}

data class TimezoneVO(
  val timeZone: String,
  val offset: Int,
) {
  companion object {
    fun of(timeZone: TimeZone) = TimezoneVO(
      timeZone = timeZone.id,
      offset = timeZone.rawOffset / (60 * 60 * 1000)
    )
  }
}
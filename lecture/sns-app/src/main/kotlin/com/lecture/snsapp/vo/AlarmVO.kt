package com.lecture.snsapp.vo

import com.lecture.snsapp.domain.Alarm
import com.lecture.snsapp.domain.AlarmArgs
import com.lecture.snsapp.domain.User
import com.lecture.snsapp.domain.enums.AlarmType
import java.sql.Timestamp

data class AlarmResponseVO(
  val id: String,
  val alarmType: AlarmType,
  val alarmArgs: AlarmArgs,
  var registerAt: Timestamp? = null,
) {
  companion object {
    fun of(alarm: Alarm) = AlarmResponseVO(
      id = alarm.id,
      alarmType = alarm.alarmType,
      alarmArgs = alarm.alarmArgs,
      registerAt = alarm.registerAt
    )
  }
}
package com.toy.springmvc.domain

import java.time.LocalDateTime

data class Event(
  var name: String,
  var limitOfEnrollment: Int,
  var startDate: LocalDateTime,
  var endDate: LocalDateTime
)
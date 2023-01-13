package com.toy.springmvc.domain

import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class Event(
  var id: String? = null,
  @field:NotBlank(groups = [NameValidate::class])
  var name: String = "",
  @field:Min(value = 0, groups = [LimitValidate::class])
  var limitOfEnrollment: Int = 0,
  var startDate: LocalDateTime = LocalDateTime.now(),
  var endDate: LocalDateTime = LocalDateTime.now()
) {
  interface LimitValidate {}
  interface NameValidate {}
}
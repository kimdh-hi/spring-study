package com.toy.springkotlin.controller.dto

import com.querydsl.core.annotations.QueryProjection

data class DeviceDto @QueryProjection constructor(
  val id: String,
  val deviceKey: String,
) {
  var someDefaultValue: String = "default"
}


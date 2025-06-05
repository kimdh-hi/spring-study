package com.toy.springkotlin.controller.dto

import com.querydsl.core.annotations.QueryProjection
import com.toy.springkotlin.entity.DeviceKey

data class DeviceDto @QueryProjection constructor(
  val id: String,
  val deviceKey: DeviceKey,
)

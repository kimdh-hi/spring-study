package com.toy.springkotlin.controller.dto

import com.querydsl.core.annotations.QueryProjection

data class DeviceDto @QueryProjection constructor(
  val id: String,
  val deviceKey: String,
) {
  //kapt -> ksp 적용시 이슈 https://github.com/IceBlizz6/querydsl-ksp/issues/9
//  var someDefaultValue: String = "default"
}


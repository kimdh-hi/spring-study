package com.toy.springkotlin.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springkotlin.controller.dto.DeviceDto
import com.toy.springkotlin.controller.dto.QDeviceDto
import com.toy.springkotlin.entity.QDevice.device

class DeviceRepositoryCustomImpl(
  private val query: JPAQueryFactory,
) : DeviceRepositoryCustom {

  override fun findByDeviceKey(deviceKey: String): DeviceDto? {
    return query.select(
      QDeviceDto(
        device.id,
        device.deviceKey,
      )
    )
      .from(device)
      .where(device.deviceKey.eq(deviceKey))
      .fetchFirst()
  }
}

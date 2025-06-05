package com.toy.springkotlin.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springkotlin.entity.Device
import com.toy.springkotlin.entity.DeviceKey
import com.toy.springkotlin.entity.QDevice
import com.toy.springkotlin.entity.QDevice.device

class DeviceRepositoryCustomImpl(
  private val query: JPAQueryFactory,
) : DeviceRepositoryCustom {

  override fun findByDeviceKey(deviceKey: DeviceKey): Device? {
    return query.selectFrom(device)
      .where(device.deviceKey.eq(deviceKey.value))
      .fetchFirst()
  }
}

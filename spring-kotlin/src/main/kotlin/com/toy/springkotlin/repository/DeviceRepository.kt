package com.toy.springkotlin.repository

import com.toy.springkotlin.entity.Device
import com.toy.springkotlin.entity.DeviceKey
import org.springframework.data.jpa.repository.JpaRepository

interface DeviceRepository : JpaRepository<Device, String>, DeviceRepositoryCustom

interface DeviceRepositoryCustom {
  fun findByDeviceKey(deviceKey: DeviceKey): Device?
}

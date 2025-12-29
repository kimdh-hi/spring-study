package com.study.jpacore.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.study.jpacore.entity.Device
import com.study.jpacore.entity.QDevice
import com.study.jpacore.entity.QDevice.device
import com.study.jpacore.entity.QUser
import com.study.jpacore.entity.QUser.user
import org.springframework.data.jpa.repository.JpaRepository

interface DeviceRepository : JpaRepository<Device, String>, DeviceRepositoryCustom {
  fun findByUserId(userId: String): Device?
}

interface DeviceRepositoryCustom {
  fun findUserByDeviceKey(deviceKey: String): Device?
}

class DeviceRepositoryCustomImpl(private val query: JPAQueryFactory) : DeviceRepositoryCustom {
  override fun findUserByDeviceKey(deviceKey: String): Device? {
    return query.selectFrom(device)
      .join(user).on(user.id.eq(device.id))
      .where(device.deviceKey.eq(deviceKey))
      .fetchFirst()
  }
}

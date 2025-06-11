package com.toy.springkotlin.repository

import com.toy.springkotlin.entity.Device
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class DeviceRepositoryTest @Autowired constructor(
  private val deviceRepository: DeviceRepository,
) {

  @Test
  fun findByDeviceKey() {
    val device = deviceRepository.save(Device.of("deviceKey"))
    deviceRepository.findByDeviceKey(device.deviceKey)
  }
}

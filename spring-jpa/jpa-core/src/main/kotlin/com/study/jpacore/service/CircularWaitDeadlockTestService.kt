package com.study.jpacore.service

import com.study.jpacore.repository.DeviceRepository
import com.study.jpacore.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CircularWaitDeadlockTestService(
  private val userRepository: UserRepository,
  private val deviceRepository: DeviceRepository,
) {
  @Transactional
  fun txUserFirst(userId: String, deviceId: String) {
    val user = userRepository.findById(userId).get()
    user.updateName("txUserFirst")

    Thread.sleep(100) // for test

    val device = deviceRepository.findById(deviceId).get()
    device.updateKey("txUserFirstKey")
  }

  @Transactional
  fun txDeviceFirst(userId: String, deviceId: String) {
    val device = deviceRepository.findById(deviceId).get()
    device.updateKey("txDeviceFirstKey")

    Thread.sleep(100) // for test

    val user = userRepository.findById(userId).get()
    user.updateName("txDeviceFirst")
  }
}

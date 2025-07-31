package com.study.jpacore.service

import com.study.jpacore.entity.Device
import com.study.jpacore.entity.User
import com.study.jpacore.repository.DeviceRepository
import com.study.jpacore.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.Executors

@SpringBootTest
class DeadlockTestServiceTest @Autowired constructor(
  private val userRepository: UserRepository,
  private val deviceRepository: DeviceRepository,
  private val circularWaitDeadlockTestService: CircularWaitDeadlockTestService,
) {
  @BeforeEach
  fun setup() {
    val user = userRepository.save(User.of("initial"))
    val device = deviceRepository.save(Device.of("initialKey"))

    println("userId = ${user.id}, deviceId = ${device.id}")
  }

  @AfterEach
  fun cleanup() {
    deviceRepository.deleteAll()
    userRepository.deleteAll()
  }

  // Deadlock found when trying to get lock; try restarting transaction
  @Test
  fun `deadlock test`() {
    val user = userRepository.findAll().first()
    val device = deviceRepository.findAll().first()

    val executor = Executors.newFixedThreadPool(2)

    val future1 = executor.submit {
      try {
        circularWaitDeadlockTestService.txUserFirst(user.id!!, device.id!!)
      } catch (e: Exception) {
        println("txUserFirst failed: ${e.message}")
      }
    }

    val future2 = executor.submit {
      try {
        circularWaitDeadlockTestService.txDeviceFirst(user.id!!, device.id!!)
      } catch (e: Exception) {
        println("txDeviceFirst failed: ${e.message}")
      }
    }

    future1.get()
    future2.get()

    executor.shutdown()
  }
}

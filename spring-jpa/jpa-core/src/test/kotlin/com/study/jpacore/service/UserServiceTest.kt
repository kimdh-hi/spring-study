package com.study.jpacore.service

import com.study.jpacore.common.QueryCounter
import com.study.jpacore.entity.Device
import com.study.jpacore.entity.User
import com.study.jpacore.repository.DeviceRepository
import com.study.jpacore.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
  private val userService: UserService,
  @Autowired private val deviceRepository: DeviceRepository,
  @Autowired private val userRepository: UserRepository,
) {

  @Test
  fun save() {
    userService.save("test")
  }

  @Test
  fun getList() {
    val device = deviceRepository.save(Device.of("deviceKey"))
    userRepository.save(User.of("test", device))

    //User-Device @ManyToOne fetchType Lazy test
    QueryCounter.set(0)
    userService.getList()
  }
}

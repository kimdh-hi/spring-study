package com.study.jpacore.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LazyConnectionTestServiceTest @Autowired constructor(
  private val lazyConnectionTestService: LazyConnectionTestService,
) {

  /**
   * log
   * hikariPool activeConnection=0
   * insert into user (name,id) values (?,?)
   * binding parameter (1:VARCHAR) <- [d175af6c-1f77-4d44-ba22-979bc50ad52f]
   * binding parameter (2:VARCHAR) <- [e2dcf6cd-a1fc-492c-9507-07883de5a6ec]
   * hikariPool activeConnection=1
   */
  @Test
  fun withDbTask() {
    lazyConnectionTestService.withDbTask()
  }

  /**
   * log
   * hikariPool activeConnection=0
   */
  @Test
  fun withoutDbTask() {
    lazyConnectionTestService.withoutDbTask()
  }

}

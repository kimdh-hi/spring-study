package com.toy.redissondistributedlock.service

import com.toy.redissondistributedlock.domain.Space
import com.toy.redissondistributedlock.repository.SpaceRepository
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class SpaceServiceTest(
  private val spaceService: SpaceService,
  private val spaceRepository: SpaceRepository
) {

  @Test
  fun lockTest() {
    //given
    val space = Space(name = "test")
    val savedSpace = spaceRepository.save(space)

    //when
    val service = Executors.newFixedThreadPool(10)
    val latch = CountDownLatch(10)
    for (i in 1..11) {
      service.execute {
        spaceService.participateWithLock(savedSpace.id!!)
        latch.countDown()
      }
    }
    latch.await()

    //then
    val participants = spaceRepository.findByIdOrNull(savedSpace.id!!)!!.participants
    println(participants)
  }

  @Test
  fun nonLockTest() {
    //given
    val space = Space(name = "test")
    val savedSpace = spaceRepository.save(space)

    //when
    val service = Executors.newFixedThreadPool(10)
    val latch = CountDownLatch(10)
    for (i in 1..10) {
      service.execute {
        spaceService.participate(savedSpace.id!!)
        latch.countDown()
      }
    }
    latch.await()

    //then
    val participants = spaceRepository.findByIdOrNull(savedSpace.id!!)!!.participants
    println(participants)
  }
}
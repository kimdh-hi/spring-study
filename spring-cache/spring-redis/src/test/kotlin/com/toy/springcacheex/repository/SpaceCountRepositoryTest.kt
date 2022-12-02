package com.toy.springcacheex.repository

import com.toy.springcacheex.domain.SpaceCount
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SpaceCountRepositoryTest(
  private val spaceCountRepository: SpaceCountRepository
){

  @Test
  fun `set-get test`() {
    val spaceId = "space01"
    val space1Ch1 = SpaceCount(spaceId, "space01-ch-01", 100)
    val space1Ch2 = SpaceCount(spaceId, "space01-ch-02", 100)
    val space1Ch3 = SpaceCount(spaceId, "space01-ch-03", 300)
    spaceCountRepository.save(space1Ch1)
    spaceCountRepository.save(space1Ch2)
    spaceCountRepository.save(space1Ch3)

    // findById 는 해당 hashKey 의 마지막 필드를 반환..
    println(spaceCountRepository.findById("space01"))

    // findAllByXXX 는 동작 안함...
    spaceCountRepository.findAllBySpaceId(spaceId).map {
      println(it)
    }
  }
}
package com.toy.springjpatsid.repository

import com.toy.springjpatsid.domain.Space
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class SpaceRepositoryImplTest @Autowired constructor(
  private val spaceRepository: SpaceRepository
) {

  @Test
  fun save() {
    //given
    (1 .. 10).forEach {
      Space(name = "space$it").also { space -> spaceRepository.save(space) }
    }

    //when
    val spaces = spaceRepository.findAll()

    //then
    println(spaces)
  }
}
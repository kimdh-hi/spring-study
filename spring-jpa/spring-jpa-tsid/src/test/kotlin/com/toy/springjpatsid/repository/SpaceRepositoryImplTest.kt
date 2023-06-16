package com.toy.springjpatsid.repository

import com.toy.springjpatsid.domain.Space
import com.toy.springjpatsid.vo.SpaceSearchVO
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

  @Test
  fun search() {
    //given
    (1 .. 20).forEach {
      Space(name = "space$it").also { space -> spaceRepository.save(space) }
    }

    val vo = SpaceSearchVO()

    //when
    val result = spaceRepository.search(vo)

    //then
    println(result)
  }
}
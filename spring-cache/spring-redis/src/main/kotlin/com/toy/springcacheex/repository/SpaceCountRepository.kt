package com.toy.springcacheex.repository

import com.toy.springcacheex.domain.SpaceCount
import org.springframework.data.repository.CrudRepository

interface SpaceCountRepository: CrudRepository<SpaceCount, String> {
  fun findAllBySpaceId(spaceId: String): List<SpaceCount>
}
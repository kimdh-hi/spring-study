package com.toy.webfluxcache.repository

import com.toy.webfluxcache.entity.TestEntity
import org.springframework.stereotype.Repository

@Repository
class TestRepository {
  suspend fun findById(id: String) = TestEntity(id, "data")
}
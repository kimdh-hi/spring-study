package com.toy.webfluxcache.vo

import com.toy.webfluxcache.entity.TestEntity

class TestResponseVO(
  val id: String,
  val data: String
) {
  companion object {
    fun of(entity: TestEntity) = TestResponseVO(id = entity.id, data = entity.data)
  }
}
package com.toy.jpacoroutine.vo

import com.toy.jpacoroutine.domain.Entity1
import com.toy.jpacoroutine.domain.Entity2

data class EntityResponse(
  val entity1s: List<Entity1>,
  val entity2s: List<Entity2>,
)
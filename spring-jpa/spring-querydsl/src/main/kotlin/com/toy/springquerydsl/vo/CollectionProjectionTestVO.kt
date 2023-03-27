package com.toy.springquerydsl.vo

import com.querydsl.core.annotations.QueryProjection

data class CollectionProjectionTestResponseVO @QueryProjection constructor(
  val entity1Id: String,
  val entity1Data: String,
  val children: List<CollectionProjectionChildResponseVO>
)

data class CollectionProjectionChildResponseVO @QueryProjection constructor(
  val entity2Id: String,
  val entity2Data: String
)
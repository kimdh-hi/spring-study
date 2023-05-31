package com.toy.springquerydsl.vo

import com.querydsl.core.annotations.QueryProjection
import com.toy.springquerydsl.common.NoArg
import com.toy.springquerydsl.domain.CollectionProjectionChild
import com.toy.springquerydsl.domain.CollectionProjectionParent
import org.slf4j.LoggerFactory

@NoArg
data class CollectionProjectionParentResponseVO @QueryProjection constructor(
  var parentId: String,
  var parentData: String,
  var children: List<CollectionProjectionChildResponseVO>
) {

  @QueryProjection constructor(collectionProjectionParent: CollectionProjectionParent) : this(
    parentId = collectionProjectionParent.id!!,
    parentData = collectionProjectionParent.data1,
    children = collectionProjectionParent.collectionProjectionChildren.map { CollectionProjectionChildResponseVO.of(it) }
  )
}

@NoArg
data class CollectionProjectionChildResponseVO @QueryProjection constructor(
  var childId: String,
  var childData: String
) {

  companion object {
    private val log = LoggerFactory.getLogger(javaClass)

    fun of(child: CollectionProjectionChild): CollectionProjectionChildResponseVO {
      log.info("child -> {}", child)
      return CollectionProjectionChildResponseVO(childId = child.id!!, childData = child.data2)
    }
  }
}

data class CollectionProjectionChildrenResponseVO(
  val children: List<CollectionProjectionChildResponseVO>
)

data class CollectionProjectionTestV2ResponseVO @QueryProjection constructor(
  val parentId: String,
  val parentData: String,
  val children: List<CollectionProjectionChild>
)
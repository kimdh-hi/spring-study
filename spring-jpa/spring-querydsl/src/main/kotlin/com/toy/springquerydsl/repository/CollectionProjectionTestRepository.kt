package com.toy.springquerydsl.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springquerydsl.domain.CollectionProjectionChildren
import com.toy.springquerydsl.domain.CollectionProjectionParent
import com.toy.springquerydsl.domain.QCollectionProjectionChildren.collectionProjectionChildren
import com.toy.springquerydsl.domain.QCollectionProjectionParent.collectionProjectionParent
import com.toy.springquerydsl.vo.CollectionProjectionTestResponseVO
import com.toy.springquerydsl.vo.QCollectionProjectionChildResponseVO
import org.springframework.data.repository.CrudRepository

interface CollectionProjectionParentRepository: CrudRepository<CollectionProjectionParent, String>, CollectionProjectionParentRepositoryCustom

interface CollectionProjectionChildRepository: CrudRepository<CollectionProjectionChildren, String>

interface CollectionProjectionParentRepositoryCustom {
  fun getAll(): List<CollectionProjectionTestResponseVO>
}

class CollectionProjectionParentRepositoryImpl(
  private val query: JPAQueryFactory
): CollectionProjectionParentRepositoryCustom {

  override fun getAll(): List<CollectionProjectionTestResponseVO> {
    return query.select(
      Projections.constructor(
        CollectionProjectionTestResponseVO::class.java,
        collectionProjectionParent.id,
        collectionProjectionParent.data1,
        Projections.list(
          QCollectionProjectionChildResponseVO(collectionProjectionChildren.id, collectionProjectionChildren.data2)
        )
      )
    )
      .from(collectionProjectionParent)
      .leftJoin(collectionProjectionParent.collectionProjectionChildren, collectionProjectionChildren)
      .fetch()
  }
}

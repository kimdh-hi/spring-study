package com.toy.springquerydsl.repository

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springquerydsl.common.QuerydslRepositorySupportCustom
import com.toy.springquerydsl.domain.CollectionProjectionChild
import com.toy.springquerydsl.domain.CollectionProjectionParent
import com.toy.springquerydsl.domain.QCollectionProjectionChild.collectionProjectionChild
import com.toy.springquerydsl.domain.QCollectionProjectionParent
import com.toy.springquerydsl.domain.QCollectionProjectionParent.collectionProjectionParent
import com.toy.springquerydsl.vo.CollectionProjectionChildResponseVO
import com.toy.springquerydsl.vo.CollectionProjectionParentResponseVO
import com.toy.springquerydsl.vo.CollectionProjectionTestV2ResponseVO
import com.toy.springquerydsl.vo.QCollectionProjectionChildResponseVO
import com.toy.springquerydsl.vo.QCollectionProjectionParentResponseVO
import com.toy.springquerydsl.vo.QCollectionProjectionTestV2ResponseVO
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.data.repository.CrudRepository

interface CollectionProjectionParentRepository: CrudRepository<CollectionProjectionParent, String>, CollectionProjectionParentRepositoryCustom

interface CollectionProjectionChildRepository: CrudRepository<CollectionProjectionChild, String>

interface CollectionProjectionParentRepositoryCustom {
  fun getAllV0(pageable: Pageable): List<CollectionProjectionParent>
  fun getAllV1(): List<CollectionProjectionParentResponseVO>
  fun getAllV2(): List<CollectionProjectionTestV2ResponseVO>
  fun getAllV3(pageable: Pageable): Slice<CollectionProjectionParentResponseVO>
  fun getAllV4(pageable: Pageable): Slice<CollectionProjectionParentResponseVO>
  fun getAllV5(pageable: Pageable): Slice<CollectionProjectionParentResponseVO>
}

class CollectionProjectionParentRepositoryImpl(
  private val query: JPAQueryFactory
): CollectionProjectionParentRepositoryCustom, QuerydslRepositorySupportCustom(CollectionProjectionParent::class.java) {
  override fun getAllV0(pageable: Pageable): List<CollectionProjectionParent> {
    return query.selectFrom(collectionProjectionParent)
      .leftJoin(collectionProjectionParent.collectionProjectionChildren, collectionProjectionChild).fetchJoin()
      .distinct()
      .offset(pageable.offset)
      .limit(pageable.pageSize + 1.toLong())
      .fetch()
  }

  override fun getAllV1(): List<CollectionProjectionParentResponseVO> {

    return query.select(
      Projections.constructor(
        CollectionProjectionParentResponseVO::class.java,
        collectionProjectionParent.id,
        collectionProjectionParent.data1,
        Projections.list(
          QCollectionProjectionChildResponseVO(collectionProjectionChild.id, collectionProjectionChild.data2)
        )
      )
    )
      .from(collectionProjectionParent)
      .leftJoin(collectionProjectionParent.collectionProjectionChildren, collectionProjectionChild)
      .distinct()
      .fetch()
  }

  override fun getAllV2(): List<CollectionProjectionTestV2ResponseVO> {
    return query.select(
      QCollectionProjectionTestV2ResponseVO(
        collectionProjectionParent.id,
        collectionProjectionParent.data1,
        collectionProjectionParent.collectionProjectionChildren
      )
    )
      .from(collectionProjectionParent)
      .leftJoin(collectionProjectionParent.collectionProjectionChildren, collectionProjectionChild)
      .distinct()
      .fetch()
  }

  override fun getAllV3(pageable: Pageable): Slice<CollectionProjectionParentResponseVO> {
    val result = query.from(collectionProjectionParent).distinct()
      .leftJoin(collectionProjectionParent.collectionProjectionChildren, collectionProjectionChild)
      .limit(pageable.pageSize.toLong())
      .offset(pageable.offset)
      .transform(
        groupBy(collectionProjectionParent.id)
          .list(
            QCollectionProjectionParentResponseVO(
              collectionProjectionParent.id,
              collectionProjectionParent.data1,
              list(
                QCollectionProjectionChildResponseVO(
                  collectionProjectionChild.id,
                  collectionProjectionChild.data2
                )
              )
            )
          )
      )

    return getSlice(result, pageable)
  }

  override fun getAllV4(pageable: Pageable): Slice<CollectionProjectionParentResponseVO> {
    val result = query.selectDistinct(collectionProjectionParent)
      .from(collectionProjectionParent).distinct()
      .leftJoin(collectionProjectionParent.collectionProjectionChildren, collectionProjectionChild).fetchJoin()
      .limit(pageable.pageSize.toLong())
      .offset(pageable.offset)
      .fetch()
      .map { parent ->
        CollectionProjectionParentResponseVO(
          parent.id!!,
          parent.data1,
          parent.collectionProjectionChildren.map { child -> CollectionProjectionChildResponseVO.of(child) }
        )
      }
      .toMutableList()

    return getSlice(result, pageable)
  }

  override fun getAllV5(pageable: Pageable): Slice<CollectionProjectionParentResponseVO> {
    val result = query.selectDistinct(
      QCollectionProjectionParentResponseVO(collectionProjectionParent)
    )
      .from(collectionProjectionParent)
      .leftJoin(collectionProjectionParent.collectionProjectionChildren, collectionProjectionChild)
      .limit(pageable.pageSize.toLong())
      .offset(pageable.offset)
      .fetch()

    return getSlice(result, pageable)
  }
}

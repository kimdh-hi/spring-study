package com.toy.springjpatsid.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springjpatsid.domain.QSpace
import com.toy.springjpatsid.domain.QSpace.space
import com.toy.springjpatsid.domain.Space
import com.toy.springjpatsid.extentions.fetchSlice
import com.toy.springjpatsid.vo.QSpaceResponseVO
import com.toy.springjpatsid.vo.SliceResponse
import com.toy.springjpatsid.vo.SpaceResponseVO
import com.toy.springjpatsid.vo.SpaceSearchVO
import org.springframework.data.jpa.repository.JpaRepository

interface SpaceRepository: JpaRepository<Space, String>, SpaceRepositoryCustom

interface SpaceRepositoryCustom {
  fun search(searchVO: SpaceSearchVO): SliceResponse<SpaceResponseVO>
}

class SpaceRepositoryImpl(
  private val query: JPAQueryFactory
): SpaceRepositoryCustom, QuerydslRepositorySupportCustom(Space::class.java) {

  override fun search(searchVO: SpaceSearchVO): SliceResponse<SpaceResponseVO> {
    return query.select(
      QSpaceResponseVO(
        space.id, space.name, space.createdDate
      )
    )
      .from(space)
      .where(
        searchVO.cursorId?.let { space.id.lt(it) },
        searchVO.searchTest?.let { space.name.containsIgnoreCase(it) }
      )
      .orderBy(space.id.desc())
      .fetchSlice(searchVO.size)
  }
}
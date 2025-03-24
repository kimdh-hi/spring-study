package com.toy.springkotlin.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.toy.springkotlin.entity.Group
import com.toy.springkotlin.entity.GroupId
import com.toy.springkotlin.entity.QGroup.group
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository : JpaRepository<Group, String>, GroupRepositoryCustom

interface GroupRepositoryCustom {
  fun findByIdCustom(id: GroupId): Group?
}

class GroupRepositoryImpl(
  private val query: JPAQueryFactory,
) : GroupRepositoryCustom {
  override fun findByIdCustom(id: GroupId): Group? {
    return query.selectFrom(group)
      .where(group.id.eq(id.value))
      .fetchFirst()
  }
}

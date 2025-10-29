package com.study.openfeignquerydsl.domain

import com.querydsl.jpa.impl.JPAQueryFactory
import com.study.openfeignquerydsl.domain.QTeam.Companion.team
import com.study.openfeignquerydsl.domain.QUser.Companion.user
import com.study.openfeignquerydsl.dto.QTeamDto
import com.study.openfeignquerydsl.dto.QUserDto
import com.study.openfeignquerydsl.dto.UserDto
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String>, UserRepositoryCustom

interface UserRepositoryCustom {
  fun findAllCustom(): List<UserDto>
}

class UserRepositoryCustomImpl(
  private val query: JPAQueryFactory,
) : UserRepositoryCustom {
  override fun findAllCustom(): List<UserDto> {
    return query.select(
      QUserDto(
        user.id,
        user.name,
        QTeamDto(team.id, team.name),
//        user.email,
      )
    )
      .from(user)
      .join(user.team, team)
      .fetch()
  }
}

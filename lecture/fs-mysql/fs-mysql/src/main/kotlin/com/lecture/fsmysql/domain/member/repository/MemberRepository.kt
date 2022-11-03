package com.lecture.fsmysql.domain.member.repository

import com.lecture.fsmysql.domain.member.entity.Member
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class MemberRepository(
  private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

  fun save(member: Member): Member {
    member.id?.let {
      update(member)
    } ?: insert(member)
    return member
  }

  private fun insert(member: Member): Member {
    val simpleJdbcInsert = SimpleJdbcInsert(namedParameterJdbcTemplate.jdbcTemplate)
      .withTableName("Member")
      .usingGeneratedKeyColumns("id")
    val parameter = BeanPropertySqlParameterSource(member)
    val id = simpleJdbcInsert.executeAndReturnKey(parameter).toLong()
    return Member(
      id = id,
      nickname = member.nickname,
      email = member.email,
      birthday = member.birthday,
    )
  }

  private fun update(member: Member): Member {

    return member
  }
}
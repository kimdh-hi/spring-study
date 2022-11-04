package com.lecture.fsmysql.domain.member.repository

import com.lecture.fsmysql.domain.member.entity.Member
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class MemberRepository(
  private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

  companion object {
    private const val TABLE = "Member"
  }

  fun save(member: Member): Member {
    return member.id?.let {
      update(member)
    } ?: insert(member)
  }

  private fun insert(member: Member): Member {
    val simpleJdbcInsert = SimpleJdbcInsert(namedParameterJdbcTemplate.jdbcTemplate)
      .withTableName(TABLE)
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

  fun findById(id: Long): Member? {
    val sql = String.format("select * from %s where id = :id", TABLE)
    val parameter = MapSqlParameterSource()
      .addValue("id", id)
    val rowMapper = RowMapper { resultSet, rowNum ->
      Member(
        id = resultSet.getLong("id"),
        nickname = resultSet.getString("nickname"),
        email = resultSet.getString("email"),
        birthday = resultSet.getObject("birthday", LocalDate::class.java),
        createdAt = resultSet.getObject("createdAt", LocalDateTime::class.java)
      )
    }
    return namedParameterJdbcTemplate.queryForObject(sql, parameter, rowMapper)
  }
}
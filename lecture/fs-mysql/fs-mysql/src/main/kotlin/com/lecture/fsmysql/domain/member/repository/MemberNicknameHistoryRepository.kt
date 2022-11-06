package com.lecture.fsmysql.domain.member.repository

import com.lecture.fsmysql.domain.member.entity.MemberNicknameHistory
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class MemberNicknameHistoryRepository(
  private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

  companion object {
    private const val TABLE = "MemberNicknameHistory"
  }

  fun save(memberNicknameHistory: MemberNicknameHistory): MemberNicknameHistory {
    return if(memberNicknameHistory.id == null)
      insert(memberNicknameHistory)
    else
      throw RuntimeException("MemberNicknameHistory does not support update.")
  }

  fun insert(memberNicknameHistory: MemberNicknameHistory): MemberNicknameHistory {
    val simpleJdbcInsert = SimpleJdbcInsert(namedParameterJdbcTemplate.jdbcTemplate)
      .withTableName(TABLE)
      .usingGeneratedKeyColumns("id")
    val parameter = BeanPropertySqlParameterSource(memberNicknameHistory)
    val id = simpleJdbcInsert.executeAndReturnKey(parameter).toLong()
    return MemberNicknameHistory(
      id = id,
      memberId = memberNicknameHistory.memberId,
      nickname = memberNicknameHistory.nickname,
      createdAt = memberNicknameHistory.createdAt
    )
  }

  fun findAllByMemberId(memberId: Long): List<MemberNicknameHistory> {
    val sql = String.format("select * from %s where memberId = :memberId", TABLE)
    val parameter = MapSqlParameterSource()
      .addValue("memberId", memberId)

    val rowMapper = RowMapper { rs, rowNum ->
      MemberNicknameHistory(
        id = rs.getLong("id"),
        memberId = rs.getLong("memberId"),
        nickname = rs.getString("nickname"),
        createdAt = rs.getObject("createdAt", LocalDateTime::class.java)
      )
    }
    return namedParameterJdbcTemplate.query(sql, parameter, rowMapper)
  }

  fun findById(id: Long): MemberNicknameHistory? {
    val sql = String.format("select * from %s where id = :id", TABLE)
    val parameter = MapSqlParameterSource()
    parameter.addValue("id", id)

    val rowMapper = RowMapper { rs, rowNum ->
      MemberNicknameHistory(
        id = rs.getLong("id"),
        memberId = rs.getLong("memberId"),
        nickname = rs.getString("nickname"),
        createdAt = rs.getObject("createdDate", LocalDateTime::class.java)
      )
    }

    return namedParameterJdbcTemplate.queryForObject(sql, parameter, rowMapper)
  }
}
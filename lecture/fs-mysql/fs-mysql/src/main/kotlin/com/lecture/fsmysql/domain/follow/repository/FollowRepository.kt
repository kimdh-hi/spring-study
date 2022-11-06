package com.lecture.fsmysql.domain.follow.repository

import com.lecture.fsmysql.domain.follow.entity.Follow
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class FollowRepository(
  private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

  companion object {
    private const val TABLE = "Follow"
  }

  fun save(follow: Follow): Follow {
    return if(follow.id == null)
      insert(follow)
    else
      throw RuntimeException("Follow does not support update.")
  }

  fun insert(follow: Follow): Follow {
    val jdbcInsert = SimpleJdbcInsert(namedParameterJdbcTemplate.jdbcTemplate)
      .withTableName(TABLE)
      .usingGeneratedKeyColumns("id")

    val parameter = BeanPropertySqlParameterSource(follow)

    val id = jdbcInsert.executeAndReturnKey(parameter).toLong()

    return Follow(
      id = id,
      fromMemberId = follow.fromMemberId,
      toMemberId = follow.toMemberId,
    )
  }

  fun findAllByFromMemberId(memberId: Long): List<Follow> {
    val sql = String.format("select * from %s where fromMemberId = :memberId", TABLE)
    val parameter = MapSqlParameterSource().addValue("memberId", memberId)
    val rowMapper = RowMapper { rs, rowNum ->
      Follow(
        id = rs.getLong("id"),
        fromMemberId = rs.getLong("fromMemberId"),
        toMemberId = rs.getLong("toMemberId"),
        createdAt = rs.getObject("createdAt", LocalDateTime::class.java)
      )
    }
    return namedParameterJdbcTemplate.query(sql, parameter, rowMapper).toList()
  }
}
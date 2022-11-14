package com.lecture.fsmysql.domain.post.repository

import com.lecture.fsmysql.domain.post.entity.Post
import com.lecture.fsmysql.domain.post.entity.Timeline
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class TimelineRepository(
  private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

  companion object {
    const val TABLE = "Timeline"

    val ROW_MAPPER = RowMapper { rs, rowNum ->
      Timeline(
        id = rs.getLong("id"),
        memberId = rs.getLong("memberId"),
        postId = rs.getLong("postId"),
        createdAt = rs.getObject("createdAt", LocalDateTime::class.java)
      )
    }

  }

  fun save(timeline: Timeline): Timeline {
    return if(timeline.id == 0L)
      insert(timeline)
    else
      throw throw RuntimeException("Timeline does not support update.")
  }

  private fun insert(timeline: Timeline): Timeline {
    val simpleJdbcInsert = SimpleJdbcInsert(namedParameterJdbcTemplate.jdbcTemplate)
      .withTableName(TABLE)
      .usingGeneratedKeyColumns("id")
    val parameter = BeanPropertySqlParameterSource(timeline)
    val id = simpleJdbcInsert.executeAndReturnKey(parameter).toLong()
    return Timeline(
      id = id,
      memberId = timeline.memberId,
      postId = timeline.postId,
      createdAt = timeline.createdAt
    )
  }

  fun bulkInsert(timelines: List<Timeline>) {
    val sql = String.format("""
      insert into %s (memberId, postId, createdAt)
      values (:memberId, :postId, :createdAt)
    """.trimIndent(), TABLE)

    val paramter = timelines
      .map { BeanPropertySqlParameterSource(it) }
      .toTypedArray()

    namedParameterJdbcTemplate.batchUpdate(sql, paramter)
  }

  fun findAllByMemberIdAndOrderByIdDesc(memberId: Long, size: Long): List<Timeline> {
    val sql = String.format("""
      select *
      from %s
      where memberId = :memberId
      order by id desc
      limit :size
    """.trimIndent(), TABLE
    )

    val parameter = MapSqlParameterSource()
      .addValue("memberId", memberId)
      .addValue("size", size)

    return namedParameterJdbcTemplate.query(sql, parameter, ROW_MAPPER).toList()
  }

  fun findAllByLessThanIdAndMemberIdAndOrderByIdDesc(id: Long, memberId: Long, size: Long): List<Timeline> {
    val sql = String.format("""
      select *
      from %s
      where memberId = :memberId and id < :id
      order by id desc
      limit :size
    """.trimIndent(), TABLE
    )

    val parameter = MapSqlParameterSource()
      .addValue("memberId", memberId)
      .addValue("size", size)
      .addValue("id", id)

    return namedParameterJdbcTemplate.query(sql, parameter, ROW_MAPPER).toList()
  }
}
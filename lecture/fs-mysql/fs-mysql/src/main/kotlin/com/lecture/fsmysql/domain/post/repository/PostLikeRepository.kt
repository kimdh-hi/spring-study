package com.lecture.fsmysql.domain.post.repository

import com.lecture.fsmysql.domain.post.entity.PostLike
import com.lecture.fsmysql.domain.post.entity.Timeline
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class PostLikeRepository(
  private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

  companion object {
    const val TABLE = "PostLike"

    val ROW_MAPPER = RowMapper { rs, rowNum ->
      PostLike(
        id = rs.getLong("id"),
        memberId = rs.getLong("memberId"),
        postId = rs.getLong("postId"),
        createdAt = rs.getObject("createdAt", LocalDateTime::class.java)
      )
    }
  }

  fun save(postLike: PostLike): PostLike {
    return if(postLike.id == 0L)
      insert(postLike)
    else
      throw throw RuntimeException("Timeline does not support update.")
  }

  private fun insert(postLike: PostLike): PostLike {
    val simpleJdbcInsert = SimpleJdbcInsert(namedParameterJdbcTemplate.jdbcTemplate)
      .withTableName(TABLE)
      .usingGeneratedKeyColumns("id")
    val parameter = BeanPropertySqlParameterSource(postLike)
    val id = simpleJdbcInsert.executeAndReturnKey(parameter).toLong()
    return PostLike(
      id = id,
      memberId = postLike.memberId,
      postId = postLike.postId,
      createdAt = postLike.createdAt
    )
  }

  fun count(postId: Long): Long {
    val sql = String.format("""
      select count(id)
      from %s
      where postId = :postId
    """.trimIndent(), TABLE)

    val parameter = MapSqlParameterSource().addValue("postId", postId)

    return namedParameterJdbcTemplate.queryForObject(sql, parameter, Long::class.java) ?: 0
  }
}
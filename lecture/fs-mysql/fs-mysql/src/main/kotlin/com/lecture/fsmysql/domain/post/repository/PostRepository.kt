package com.lecture.fsmysql.domain.post.repository

import com.lecture.fsmysql.domain.member.entity.Member
import com.lecture.fsmysql.domain.member.repository.MemberRepository
import com.lecture.fsmysql.domain.post.dto.DailyPostCountRequestDto
import com.lecture.fsmysql.domain.post.dto.DailyPostCountResponseDto
import com.lecture.fsmysql.domain.post.entity.Post
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class PostRepository(
  private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate
) {

  companion object {
    private const val TABLE = "Post"

    val ROW_MAPPER = RowMapper { rs, rowNum ->
      Post(
        id = rs.getLong("id"),
        memberId = rs.getLong("memberId"),
        content = rs.getString("content"),
        createdAt = rs.getObject("createdAt", LocalDateTime::class.java)
      )
    }

    val DAILY_POST_COUNT_MAPPER = RowMapper { rs, rowNum ->
      DailyPostCountResponseDto(
        memberId = rs.getLong("memberId"),
        date = rs.getObject("createdAt", LocalDate::class.java),
        postCount = rs.getLong("count")
      )
    }
  }

  fun save(post: Post): Post {
    return if(post.id == 0L)
      insert(post)
    else
      update(post)
//    throw throw RuntimeException("Post does not support update.")
  }

  private fun insert(post: Post): Post {
    val simpleJdbcInsert = SimpleJdbcInsert(namedParameterJdbcTemplate.jdbcTemplate)
      .withTableName(TABLE)
      .usingGeneratedKeyColumns("id")
    val parameter = BeanPropertySqlParameterSource(post)
    val id = simpleJdbcInsert.executeAndReturnKey(parameter).toLong()
    return Post(
      id = id,
      memberId = post.memberId,
      content = post.content
    )
  }

  private fun update(post: Post): Post {
    val sql = String.format("""
      update %s 
      set memberId = :memberId, content = :content, createdAt = :createdAt
      where id = :id
    """.trimIndent(), TABLE
    )
    val params = BeanPropertySqlParameterSource(post)
    namedParameterJdbcTemplate.update(sql, params)
    return post
  }

  fun groupByCreatedDate(requestDto: DailyPostCountRequestDto): List<DailyPostCountResponseDto> {
    val sql = String.format("""
      select createdAt, memberId, count(id) as count
      from %s
      where memberId = :memberId and createdAt between :firstDate and :lastDate
      group by createdAt, memberId
    """.trimIndent(), TABLE)
    val parameter = BeanPropertySqlParameterSource(requestDto)
    return namedParameterJdbcTemplate.query(sql, parameter, DAILY_POST_COUNT_MAPPER).toList()
  }

  fun bulkInsert(posts: List<Post>) {
    val sql = String.format("""
      insert into %s (memberId, content, createdAt)
      values (:memberId, :content, :createdAt)
    """.trimIndent(), TABLE)

    val parameters = posts
      .map { BeanPropertySqlParameterSource(it) }
      .toTypedArray()

    namedParameterJdbcTemplate.batchUpdate(sql, parameters)
  }
}
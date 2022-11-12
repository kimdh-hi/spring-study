package com.lecture.fsmysql.domain.post.repository

import com.lecture.fsmysql.common.PageHelper
import com.lecture.fsmysql.domain.post.dto.DailyPostCountRequestDto
import com.lecture.fsmysql.domain.post.dto.DailyPostCountResponseDto
import com.lecture.fsmysql.domain.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
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

  fun findAllByMemberId(memberId: Long, pageable: Pageable): Page<Post> {
    val sql = String.format("""
      select * 
      from %s
      where memberId = :memberId
      order by %s
      limit :size
      offset :offset      
    """.trimIndent(), TABLE, PageHelper.getSort(pageable.sort))

    val parameter = MapSqlParameterSource()
      .addValue("memberId", memberId)
      .addValue("size", pageable.pageSize)
      .addValue("offset", pageable.offset)

    val posts = namedParameterJdbcTemplate.query(sql, parameter, ROW_MAPPER).toList()
    return PageImpl(posts, pageable, getTotalCount(memberId))
  }

  private fun getTotalCount(memberId: Long): Long {
    val sql = String.format("""
      select count(id)
      from %s
      where memberId = :memberId
    """.trimIndent(), TABLE)

    val parameter = MapSqlParameterSource()
      .addValue("memberId", memberId)

    return namedParameterJdbcTemplate.queryForObject(sql, parameter, Long::class.java)!!
  }

  fun findAllByMemberIdAndOrderByIdDesc(memberId: Long, size: Long): List<Post> {
    val sql = String.format("""
      select *
      from %s
      where memberId = :memberId
      order by id desc
      limit :size
    """.trimIndent(), TABLE)

    val parameter = MapSqlParameterSource()
      .addValue("memberId", memberId)
      .addValue("size", size)

    return namedParameterJdbcTemplate.query(sql, parameter, ROW_MAPPER).toList()
  }

  fun findAllByLessThanIdAndMemberIdAndOrderByIdDesc(id: Long, memberId: Long, size: Long): List<Post> {
    val sql = String.format("""
      select *
      from %s
      where memberId = :memberId and id < :id
      order by id desc
      limit :size
    """.trimIndent(), TABLE)

    val parameter = MapSqlParameterSource()
      .addValue("memberId", memberId)
      .addValue("size", size)
      .addValue("id", id)

    return namedParameterJdbcTemplate.query(sql, parameter, ROW_MAPPER).toList()
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
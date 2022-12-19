package com.toy.springcacheex.service

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class RankingService(
  private val redisTemplate: StringRedisTemplate
) {

  companion object {
    const val RANKING_BOARD_KEY = "rankingBoard"
  }

  fun setScore(userId: String, score: Int) {
    val zSetOps = redisTemplate.opsForZSet()
    zSetOps.add(RANKING_BOARD_KEY, userId, score.toDouble())
  }

  fun getRaking(userId: String): Long {
    val zSetOps = redisTemplate.opsForZSet()
    return zSetOps.reverseRank(RANKING_BOARD_KEY, userId) ?: throw RuntimeException("not exists user...")
  }

  fun getTopNRank(limit: Long): List<RankingResponseVO> {
    val zSetOps = redisTemplate.opsForZSet()
    return zSetOps.reverseRangeWithScores(RANKING_BOARD_KEY, 0, limit-1)
      ?.map { RankingResponseVO(it.value, it.score?.toInt()) } ?: listOf()
  }
}

data class RankingResponseVO(
  val userId: String?,
  val score: Int?
)
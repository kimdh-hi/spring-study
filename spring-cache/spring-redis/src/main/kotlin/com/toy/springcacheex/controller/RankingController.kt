package com.toy.springcacheex.controller

import com.toy.springcacheex.service.RankingResponseVO
import com.toy.springcacheex.service.RankingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ranking")
class RankingController(
  private val rankingService: RankingService
) {

  @PostMapping("/{userId}/{score}")
  fun setScore(@PathVariable userId: String, @PathVariable score: Int): String {
    rankingService.setScore(userId, score)
    return "ok"
  }

  @GetMapping("/{userId}")
  fun getScore(@PathVariable userId: String): Long {
    return rankingService.getRaking(userId)
  }

  @GetMapping("/top/{limit}")
  fun getScores(@PathVariable limit: Long): List<RankingResponseVO> {
    return rankingService.getTopNRank(limit)
  }
}
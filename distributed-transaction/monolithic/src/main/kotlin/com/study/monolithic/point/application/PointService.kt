package com.study.monolithic.point.application

import com.study.monolithic.point.infa.PointRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointService(
  private val pointRepository: PointRepository,
) {

  @Transactional
  fun use(userId: Long, amount: Long) {
    val point = pointRepository.findByUserId(userId) ?: throw RuntimeException("point not found. userId=$userId")
    point.use(amount)
  }
}

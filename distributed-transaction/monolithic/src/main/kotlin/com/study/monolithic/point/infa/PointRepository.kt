package com.study.monolithic.point.infa

import com.study.monolithic.point.domain.Point
import org.springframework.data.jpa.repository.JpaRepository

interface PointRepository : JpaRepository<Point, Long> {
  fun findByUserId(userId: Long): Point?
}

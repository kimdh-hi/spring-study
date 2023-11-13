package com.toy.hibernate6.repository

import com.toy.hibernate6.domain.TimeBasedUUID
import org.springframework.data.jpa.repository.JpaRepository

interface TimeBasedUUIDRepository: JpaRepository<TimeBasedUUID, String> {
  fun findAllByOrderByIdDesc(): List<TimeBasedUUID>
}
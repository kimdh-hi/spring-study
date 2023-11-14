package com.toy.hibernate6.repository

import com.toy.hibernate6.domain.RandomBasedUUID
import com.toy.hibernate6.domain.TimeBasedUUID
import org.springframework.data.jpa.repository.JpaRepository

interface RandomBasedUUIDRepository: JpaRepository<RandomBasedUUID, String> {
  fun findAllByOrderByIdDesc(): List<TimeBasedUUID>
}
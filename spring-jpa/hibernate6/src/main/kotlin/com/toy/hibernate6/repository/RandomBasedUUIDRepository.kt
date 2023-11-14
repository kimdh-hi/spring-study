package com.toy.hibernate6.repository

import com.toy.hibernate6.domain.RandomUUID
import com.toy.hibernate6.domain.TimeBasedUUID
import org.springframework.data.jpa.repository.JpaRepository

interface RandomUUIDRepository: JpaRepository<RandomUUID, String> {
  fun findAllByOrderByIdDesc(): List<TimeBasedUUID>
}
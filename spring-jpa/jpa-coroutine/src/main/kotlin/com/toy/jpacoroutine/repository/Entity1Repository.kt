package com.toy.jpacoroutine.repository

import com.toy.jpacoroutine.domain.Entity1
import org.springframework.data.jpa.repository.JpaRepository

interface Entity1Repository: JpaRepository<Entity1, String> {
  fun findAllByDataContaining(keyword: String): List<Entity1>
}
package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.EmbeddableTestEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EmbeddableTestEntityRepository: JpaRepository<EmbeddableTestEntity, String> {
}

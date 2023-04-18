package com.toy.jpabasic.repository

import com.toy.jpabasic.domain.EnumCollectionTestEntity
import org.springframework.data.repository.CrudRepository

interface EnumCollectionTestEntityRepository: CrudRepository<EnumCollectionTestEntity, String> {
}
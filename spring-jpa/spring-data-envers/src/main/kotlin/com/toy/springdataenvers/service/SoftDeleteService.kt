package com.toy.springdataenvers.service

import com.toy.springdataenvers.domain.SoftDeleteEntity
import com.toy.springdataenvers.repository.SoftDeleteEntityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true)
class SoftDeleteService(
  private val softDeleteEntityRepository: SoftDeleteEntityRepository,
) {

  @Transactional
  fun save(): SoftDeleteEntity {
    return SoftDeleteEntity(data = UUID.randomUUID().toString()).also {
      softDeleteEntityRepository.save(it)
    }
  }

  @Transactional
  fun delete(id: String) {
    softDeleteEntityRepository.deleteById(id)
  }
}
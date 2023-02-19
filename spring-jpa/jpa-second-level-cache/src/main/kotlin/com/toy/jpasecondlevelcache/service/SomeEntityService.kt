package com.toy.jpasecondlevelcache.service

import com.toy.jpasecondlevelcache.domain.SomeEntity
import com.toy.jpasecondlevelcache.repository.SomeEntityRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SomeEntityService(
  private val someEntityRepository: SomeEntityRepository
) {

  fun find(id: Long): SomeEntity {
    return someEntityRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("not found...")
  }
}
package com.lecture.catalogservice.service

import com.lecture.catalogservice.repository.CatalogRepository
import com.lecture.catalogservice.vo.CatalogResponseVO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CatalogService(
  private val catalogRepository: CatalogRepository
) {

  fun findAll() = catalogRepository.findAll()
    .map { CatalogResponseVO.of(it) }

}
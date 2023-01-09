package com.lecture.productservice.service

import com.lecture.productservice.repository.ProductRepository
import com.lecture.productservice.vo.ProductResponseVO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
  private val productRepository: ProductRepository
) {

  fun findAll() = productRepository.findAll()
    .map { ProductResponseVO.of(it) }

}
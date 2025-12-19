package com.study.monolithic.init

import com.study.monolithic.point.domain.Point
import com.study.monolithic.point.infa.PointRepository
import com.study.monolithic.product.domian.Product
import com.study.monolithic.product.infra.ProductRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class TestDataCreator(
  private val pointRepository: PointRepository,
  private val productRepository: ProductRepository,
) {

  @PostConstruct
  fun create() {
    pointRepository.save(Point.of(1L, 10_000L))

    val product1 = Product.of(100L, 100L)
    val product2 = Product.of(100L, 200L)
    productRepository.save(product1)
    productRepository.save(product2)
  }
}

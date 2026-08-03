package com.study.heapdump.service

import com.study.heapdump.domain.Product
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SeedService {

  @PersistenceContext
  private lateinit var em: EntityManager

  @Transactional
  fun seed(count: Int): Int {
    for (i in 1..count) {
      em.persist(newProduct(i))
      if (i % CHUNK_SIZE == 0) {
        em.flush()
        em.clear()
      }
    }
    return count
  }

  @Transactional
  fun seedWithoutClear(count: Int): Int {
    for (i in 1..count) {
      em.persist(newProduct(i))
    }
    return count
  }

  private fun newProduct(i: Int) = Product(
    name = "product-$i",
    description = "description-$i-".repeat(20),
    price = i.toLong(),
  )

  companion object {
    private const val CHUNK_SIZE = 1_000
  }
}

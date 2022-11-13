package com.lecture.passbatch.repository

import com.lecture.passbatch.domain.Package
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface PackageRepository: JpaRepository<Package, Int> {
  fun findByCreatedAtAfter(dateTime: Any, pageable: Pageable): List<Package>

  @Query(
    """
      update Package p
      set p.count = :count, p.period = :period
      where p.packageSeq = :packageSeq
    """
  )
  @Transactional
  @Modifying
  fun updateCountAndPeriod(packageSeq: Int, count: Int, period: Int): Int
}
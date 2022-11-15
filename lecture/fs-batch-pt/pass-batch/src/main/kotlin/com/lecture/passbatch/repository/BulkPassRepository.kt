package com.lecture.passbatch.repository

import com.lecture.passbatch.domain.BulkPass
import com.lecture.passbatch.domain.enums.BulkPassStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface BulkPassRepository: JpaRepository<BulkPass, Int> {
  fun findByStatusAndStartedAtGratherThan(status: BulkPassStatus, startedAt: LocalDateTime)
}
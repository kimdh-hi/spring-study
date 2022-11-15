package com.lecture.passbatch.domain

import com.lecture.passbatch.domain.enums.BulkPassStatus
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "bulk_pass")
class BulkPass(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var bulkPassSeq: Int = 0,
  val packageSeq: Int,
  val userGroupId: String,

  @Enumerated(EnumType.STRING)
  var status: BulkPassStatus,
  var count: Int,

  var startedAt: LocalDateTime,
  var endedAt: LocalDateTime,
  )
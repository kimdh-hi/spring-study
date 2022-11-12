package com.lecture.passbatch.domain

import com.lecture.passbatch.domain.enums.PassStatus
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "pass")
class Pass(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var passSeq: Int = 0,
  val packageSeq: Int,
  val userId: String,

  @Enumerated(EnumType.STRING)
  var status: PassStatus,
  var remainingCount: Int,

  var startedAt: LocalDateTime,
  var endedAt: LocalDateTime,
  var expiredAt: LocalDateTime
): BaseEntity()
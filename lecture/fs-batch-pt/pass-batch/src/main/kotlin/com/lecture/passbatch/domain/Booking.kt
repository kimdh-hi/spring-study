package com.lecture.passbatch.domain

import com.lecture.passbatch.domain.enums.BookingStatus
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "booking")
class Booking(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var bookingSeq: Int = 0,
  var passSeq: Int,
  var userId: String,

  @Enumerated(EnumType.STRING)
  var status: BookingStatus,
  var usedPass: Boolean,
  var attended: Boolean,

  var startedAt: LocalDateTime,
  var endedAt: LocalDateTime,
  var canceledAt: LocalDateTime
): BaseEntity()
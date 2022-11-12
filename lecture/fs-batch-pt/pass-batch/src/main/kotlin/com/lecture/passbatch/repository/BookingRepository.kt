package com.lecture.passbatch.repository

import com.lecture.passbatch.domain.Booking
import org.springframework.data.jpa.repository.JpaRepository

interface BookingRepository: JpaRepository<Booking, Int> {
}
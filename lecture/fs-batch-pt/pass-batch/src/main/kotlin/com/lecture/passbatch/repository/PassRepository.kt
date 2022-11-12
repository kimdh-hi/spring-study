package com.lecture.passbatch.repository

import com.lecture.passbatch.domain.Pass
import org.springframework.data.jpa.repository.JpaRepository

interface PassRepository: JpaRepository<Pass, Int> {
}
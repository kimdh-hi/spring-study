package com.lecture.springbatchbasic.core.repository

import com.lecture.springbatchbasic.core.domain.ResultText
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ResultTextRepository: JpaRepository<ResultText, Long> {
}
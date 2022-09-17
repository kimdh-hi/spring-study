package com.lecture.springbatchbasic.repository

import com.lecture.springbatchbasic.domain.ResultText
import org.springframework.data.jpa.repository.JpaRepository

interface ResultTextRepository: JpaRepository<ResultText, Long> {
}
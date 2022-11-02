package com.lecture.snsapp.repository

import com.lecture.snsapp.domain.Alarm
import com.lecture.snsapp.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface AlarmRepository: JpaRepository<Alarm, String> {

  fun findAllByUser(user: User, pageable: Pageable): Page<Alarm>
  fun findAllByUserId(userId: String, pageable: Pageable): Page<Alarm>
}
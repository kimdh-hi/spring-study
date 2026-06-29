package com.toy.springquerydsl.repository

import com.toy.springquerydsl.domain.Team
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : JpaRepository<Team, Long> {
}

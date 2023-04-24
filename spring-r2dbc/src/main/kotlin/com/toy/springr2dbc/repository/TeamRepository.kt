package com.toy.springr2dbc.repository

import com.toy.springr2dbc.domain.Team
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository: JpaRepository<Team, String> {
}
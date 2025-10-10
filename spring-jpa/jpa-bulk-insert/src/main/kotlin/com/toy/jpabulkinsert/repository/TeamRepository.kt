package com.toy.jpabulkinsert.repository

import com.toy.jpabulkinsert.entity.Team
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : JpaRepository<Team, String> {
}

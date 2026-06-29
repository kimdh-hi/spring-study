package com.study.multitenancy.domain.repository

import com.study.multitenancy.domain.model.Space
import org.springframework.data.jpa.repository.JpaRepository

interface SpaceRepository : JpaRepository<Space, Long>

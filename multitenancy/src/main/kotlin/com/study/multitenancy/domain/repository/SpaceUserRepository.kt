package com.study.multitenancy.domain.repository

import com.study.multitenancy.domain.model.SpaceUser
import org.springframework.data.jpa.repository.JpaRepository

interface SpaceUserRepository : JpaRepository<SpaceUser, Long>

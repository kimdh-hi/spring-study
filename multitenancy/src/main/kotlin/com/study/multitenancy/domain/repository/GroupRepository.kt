package com.study.multitenancy.domain.repository

import com.study.multitenancy.domain.model.Group
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository : JpaRepository<Group, Long>

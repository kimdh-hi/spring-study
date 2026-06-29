package com.study.multitenancy.domain.repository

import com.study.multitenancy.domain.model.GroupUser
import org.springframework.data.jpa.repository.JpaRepository

interface GroupUserRepository : JpaRepository<GroupUser, Long>

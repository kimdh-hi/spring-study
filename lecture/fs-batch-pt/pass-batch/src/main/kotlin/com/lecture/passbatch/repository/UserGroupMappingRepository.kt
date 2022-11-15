package com.lecture.passbatch.repository

import com.lecture.passbatch.domain.UserGroupMapping
import org.springframework.data.jpa.repository.JpaRepository

interface UserGroupMappingRepository: JpaRepository<UserGroupMapping, Int> {
  fun findByUserGroupId(userGroupId: String): List<UserGroupMapping>
}
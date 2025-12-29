package com.study.jpacore.repository

import com.study.jpacore.entity.UserData
import org.springframework.data.jpa.repository.JpaRepository

interface UserDataRepository : JpaRepository<UserData, String> {
}

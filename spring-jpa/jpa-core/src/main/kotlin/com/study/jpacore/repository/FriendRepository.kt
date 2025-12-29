package com.study.jpacore.repository

import com.study.jpacore.entity.Friend
import org.springframework.data.jpa.repository.JpaRepository

interface FriendRepository : JpaRepository<Friend, String> {
}

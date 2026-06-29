package com.study.readwritesplitting.domain.user.repository

import com.study.readwritesplitting.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, String> {

  @Query(
    value = "SELECT @@server_id AS serverId, @@hostname AS hostname, @@read_only AS readOnly",
    nativeQuery = true,
  )
  fun whereAmI(): WhereAmI
}

interface WhereAmI {
  fun getServerId(): Long
  fun getHostname(): String
  fun getReadOnly(): Int
}

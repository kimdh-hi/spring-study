package com.toy.consumer.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.toy.consumer.base.NoArg
import java.time.LocalDateTime

@NoArg
data class User (
   var id: String,
   var name: String,
   @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   var createdDate: LocalDateTime,
   var role: UserRole? = null
)

enum class UserRole {
   ADMIN, USER
}
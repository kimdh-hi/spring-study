package com.toy.consumer.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.toy.consumer.base.NoArg
import java.time.LocalDateTime

@NoArg
data class Employee (
   var id: String,
   var name: String,
   @field:JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   var createdDate: LocalDateTime
)
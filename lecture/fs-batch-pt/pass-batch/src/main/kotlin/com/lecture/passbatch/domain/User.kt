package com.lecture.passbatch.domain

import com.lecture.passbatch.domain.enums.UserStatus
import javax.persistence.*

@Entity
@Table(name = "user")
class User(
  @Id
  var userId: String = "",
  var userName: String,
  @Enumerated(EnumType.STRING)
  var status: UserStatus,
  var phone: String,
  var meta: String
): BaseEntity()
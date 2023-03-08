package com.toy.springredisevent.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class UserStatus(
  @Column(name = "status_name")
  val statusName: String? = null,

  @Column(name = "status_icon_path")
  val statusIconPath: String? = null
)
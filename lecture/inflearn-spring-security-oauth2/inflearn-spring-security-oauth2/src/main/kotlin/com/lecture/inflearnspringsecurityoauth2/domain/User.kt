package com.lecture.inflearnspringsecurityoauth2.domain

import org.hibernate.annotations.GenericGenerator
import org.springframework.security.core.GrantedAuthority
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,
  val username: String,
  val name: String,
  val password: String,
  val registrationId: String,
//  val authorities: List<GrantedAuthority>
)
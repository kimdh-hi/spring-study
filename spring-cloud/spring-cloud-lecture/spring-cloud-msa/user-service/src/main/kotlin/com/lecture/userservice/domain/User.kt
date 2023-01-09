package com.lecture.userservice.domain

import javax.persistence.*

@Entity
@Table(name = "users")
class User(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? =  null,

  @Column(nullable = false, unique = true)
  var email: String,

  @Column(nullable = false)
  var name: String,

  @Column(nullable = false, unique = true)
  var userId: String,

  @Column(nullable = false)
  var password: String
)
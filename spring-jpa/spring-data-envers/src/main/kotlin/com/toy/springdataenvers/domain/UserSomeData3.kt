package com.toy.springdataenvers.domain

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.envers.Audited

@Entity
@Table(name = "tb_user_some_data3")
@Audited
class UserSomeData3(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  val data: String,

  @Column(name = "user_id", nullable = false, updatable = false)
  val userId: String
)
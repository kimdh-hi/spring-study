package com.toy.springdataenvers.domain

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.envers.Audited

@Entity
@Table(name = "tb_user_some_data1")
@Audited
class UserSomeData1(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  val data: String,

  @ManyToOne
  @JoinColumn(name = "user_id")
  val user: User
)
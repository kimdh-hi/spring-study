package com.toy.springdataenvers.domain

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.UuidGenerator
import org.hibernate.envers.Audited

@Entity
@Table(name = "tb_user_some_data2")
@Audited
class UserSomeData2(
  @Id
  @UuidGenerator
  var id: String? = null,

  val data: String,

  @ManyToOne
  @JoinColumn(name = "user_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  val user: User
)

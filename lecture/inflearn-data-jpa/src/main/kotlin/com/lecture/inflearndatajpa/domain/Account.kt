package com.lecture.inflearndatajpa.domain

import javax.persistence.*

@Entity
class Account(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? =  null,

  @Column(nullable = false, unique = true)
  var username: String,

  @Column(nullable = false)
  var password: String,

  @Embedded
  @AttributeOverrides(
    AttributeOverride(name = "street", column = Column(name = "custom_street"))
  )
  var address: Address,

) {
  companion object {
    fun of(
      username: String,
      password: String,
      address: Address
    ) = Account(
      username = username,
      password = password,
      address = address,
    )
  }
}
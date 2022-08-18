package com.toy.oauthclientoidc.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "tb_user")
class User(

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(length = 50)
  var id: String? = null,

  var name: String,
  var username: String,
  var password: String
)
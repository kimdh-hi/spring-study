package com.toy.jpaeventlistener.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Post(
  @Id
  @GeneratedValue
  var id: Long? = null,

  var title: String
)
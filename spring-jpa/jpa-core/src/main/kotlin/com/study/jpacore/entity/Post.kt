package com.study.jpacore.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Post private constructor(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  @Column(length = 100, nullable = false)
  var title: String,
) {

  companion object {
    fun of(title: String) = Post(title = title)
  }
}

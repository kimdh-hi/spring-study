package com.study.springgraalvm.domain.model

import com.study.springgraalvm.common.BaseTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
  var name: String,

  var email: String,

  @ManyToOne(fetch = FetchType.LAZY)
  val company: Company,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,
) : BaseTimeEntity()

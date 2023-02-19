package com.toy.jpasecondlevelcache.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
class SomeEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = -1,

  var data: String,
)
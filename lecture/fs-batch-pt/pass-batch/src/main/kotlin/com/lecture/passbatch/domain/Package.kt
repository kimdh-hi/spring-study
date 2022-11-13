package com.lecture.passbatch.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "package")
class Package(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var packageSeq: Int? = null,

  var packageName: String,
  var count: Int = 0,
  var period: Int
): BaseEntity()
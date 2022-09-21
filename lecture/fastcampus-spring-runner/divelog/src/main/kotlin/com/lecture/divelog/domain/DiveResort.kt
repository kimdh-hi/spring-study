package com.lecture.divelog.domain

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class DiveResort(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
  var name: String,
  var ownerName: String,
  var contactNumber: String,
  var address: String,
  var description: String,
  var createdDate: LocalDateTime,
  var lastModifiedDate: LocalDateTime
) {

}
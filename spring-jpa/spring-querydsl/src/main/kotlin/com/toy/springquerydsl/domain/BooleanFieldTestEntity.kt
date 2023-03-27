package com.toy.springquerydsl.domain

import org.hibernate.annotations.GenericGenerator
import org.jetbrains.annotations.PropertyKey
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Table
@Entity(name = "mt_boolean_field_test")
class BooleanFieldTestEntity(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  @Column(name = "is_default")
  val isDefault: Boolean = false
)
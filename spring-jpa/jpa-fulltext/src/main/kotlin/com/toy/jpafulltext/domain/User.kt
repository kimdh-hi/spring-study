package com.toy.jpafulltext.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table

@Table(
  indexes = [
    Index(name = "idx_user_desc1", columnList = "description1"),
    Index(name = "idx_user_desc2", columnList = "description2")
  ]
)
@Entity(name = "tb_user")
class User(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

  var name: String,

  @Column(columnDefinition = "VARCHAR(2048) NOT NULL, FULLTEXT KEY description_full_text (description1)")
  var description1: String,

  var description2: String
)
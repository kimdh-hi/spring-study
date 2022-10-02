package com.toy.jpajooq.domain

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Table
@Entity(name = "tb_group")
class Group(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(length = 50)
  var id: String? = null,

  @Column(nullable = false)
  var name: String,
)
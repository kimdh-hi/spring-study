package com.lecture.springbatchbasic.core.domain

import javax.persistence.*

@Entity
@Table(name = "result_text")
class ResultText(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
  val text: String
)
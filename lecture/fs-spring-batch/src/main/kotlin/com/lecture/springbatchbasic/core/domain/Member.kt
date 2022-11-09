package com.lecture.springbatchbasic.core.domain

import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

data class Member1VO(
  val id: Long,
  val name: String,
  val age: Int
)

data class Member2VO(
  val id: Long,
  val name: String,
  val age: Int,
  val type: String
)

class MemberFieldSetMapper: FieldSetMapper<Member1VO> {
  override fun mapFieldSet(fieldSet: FieldSet): Member1VO {
    return Member1VO(
      id = fieldSet.readLong(0),
      name = fieldSet.readString(1),
      age = fieldSet.readInt(2)
    )
  }
}
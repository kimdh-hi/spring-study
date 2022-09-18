package com.lecture.springbatchbasic.core.domain

import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

data class AmountVO(
  val id: Int,
  val name: String,
  var amount: Long
)

class AmountFieldSetMapper: FieldSetMapper<AmountVO> {
  override fun mapFieldSet(fieldSet: FieldSet): AmountVO {
    return AmountVO(
      id = fieldSet.readInt(0),
      name = fieldSet.readString(1),
      amount = fieldSet.readLong(2)
    )
  }
}


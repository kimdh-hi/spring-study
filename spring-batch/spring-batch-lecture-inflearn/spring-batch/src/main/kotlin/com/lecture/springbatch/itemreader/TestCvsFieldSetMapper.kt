package com.lecture.springbatch.itemreader

import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

class TestCvsFieldSetMapper: FieldSetMapper<TestCsv> {
  override fun mapFieldSet(fieldSet: FieldSet): TestCsv {
    return TestCsv(
      fieldSet.readString(0),
      fieldSet.readString(1),
      fieldSet.readInt(2)
    )
  }
}

data class TestCsv(
  val id: String,
  val name: String,
  val age: Int
)
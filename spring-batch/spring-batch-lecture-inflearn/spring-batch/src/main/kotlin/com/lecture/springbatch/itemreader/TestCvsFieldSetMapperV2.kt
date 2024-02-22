package com.lecture.springbatch.itemreader

import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

class TestCvsFieldSetMapperV2: FieldSetMapper<TestCsv> {
  override fun mapFieldSet(fieldSet: FieldSet): TestCsv {
    return TestCsv(
      fieldSet.readString("id"),
      fieldSet.readString("name"),
      fieldSet.readInt("age")
    )
  }
}

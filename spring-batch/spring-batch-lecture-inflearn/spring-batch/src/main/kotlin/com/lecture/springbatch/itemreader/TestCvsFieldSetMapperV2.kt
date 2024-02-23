package com.lecture.springbatch.itemreader

import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

class TestCvsFieldSetMapperV2: FieldSetMapper<Test2Csv> {
  override fun mapFieldSet(fieldSet: FieldSet): Test2Csv {
    return Test2Csv(
      fieldSet.readString("name"),
      fieldSet.readInt("year"),
      fieldSet.readInt("age")
    )
  }
}

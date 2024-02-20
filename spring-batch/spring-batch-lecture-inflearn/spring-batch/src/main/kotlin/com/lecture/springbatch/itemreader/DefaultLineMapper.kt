package com.lecture.springbatch.itemreader

import org.springframework.batch.item.file.LineMapper
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.LineTokenizer

class DefaultLineMapper<T>: LineMapper<T> {

  private lateinit var tokenizer: LineTokenizer
  private lateinit var fieldSetMapper: FieldSetMapper<T>

  override fun mapLine(line: String, lineNumber: Int): T & Any {
    return fieldSetMapper.mapFieldSet(tokenizer.tokenize(line))
  }

  fun setLineTokenizer(lineTokenizer: LineTokenizer) {
    this.tokenizer = lineTokenizer
  }

  fun setFieldSetMapper(fieldSetMapper: FieldSetMapper<T>) {
    this.fieldSetMapper = fieldSetMapper
  }
}
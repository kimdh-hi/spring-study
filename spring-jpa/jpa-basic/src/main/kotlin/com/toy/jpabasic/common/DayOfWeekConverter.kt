package com.toy.jpabasic.common

import java.time.DayOfWeek
import java.util.*
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class DayOfWeekConverter : AttributeConverter<EnumSet<DayOfWeek>, String> {

  override fun convertToDatabaseColumn(attribute: EnumSet<DayOfWeek>): String {
    return attribute.joinToString(",") { it.name }
  }

  override fun convertToEntityAttribute(dbData: String): EnumSet<DayOfWeek> {
    val daysOfWeek = dbData.split(",").map { DayOfWeek.valueOf(it) }.toSet()
    return EnumSet.copyOf(DayOfWeek.values().filter { daysOfWeek.contains(it) })
  }
}
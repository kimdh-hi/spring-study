package com.toy.jpabasic.domain

import com.toy.jpabasic.common.DayOfWeekConverter
import org.hibernate.annotations.GenericGenerator
import java.time.DayOfWeek
import java.util.*
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Table
@Entity(name = "enum_collection_test")
class EnumCollectionTestEntity(
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  var id: String? = null,

//  @Convert(converter = DayOfWeekConverter::class)
  var weeks: EnumSet<DayOfWeek> = EnumSet.noneOf(DayOfWeek::class.java)
) {
  override fun toString(): String {
    return "EnumCollectionTestEntity(id=$id, weeks=$weeks)"
  }
}

enum class Week {
  MONDAY,
  TUESDAY,
  WEDNESDAY,
  THURSDAY,
  FRIDAY,
  SATURDAY,
  SUNDAY;
}
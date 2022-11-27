package com.sample.`02-movie`

import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

class Movie(
  val title: String,
  val runningTime: String,
  val fee: Money,
  val discountConditions: MutableList<DiscountCondition>,

  val movieType: MovieType,
  val discountAmountL: Money,
  val discountPercent: Double
)

enum class MovieType {
  AMOUNT_DISCOUNT,
  PERCENT_DISCOUNT,
  NONE_DISCOUNT
}

class Money(
  val amount: BigDecimal
) {
  companion object {

    val ZERO = wons(0)

    fun wons(amount: Long): Money {
      return Money(BigDecimal.valueOf(amount))
    }
  }

  fun plus(amount: Money): Money {
    return Money(this.amount.add(amount.amount))
  }

  fun minus(amount: Money): Money {
    return Money(this.amount.subtract(amount.amount))
  }

  fun times(percent: Double) {
    Money(this.amount.multiply(BigDecimal.valueOf(percent)))
  }

  fun isLessThan(other: Money) = amount < other.amount
  fun isGreatherThanOrEqual(other: Money) = amount > other.amount
}

enum class DiscountConditionType {
  SEQUENCE, PERIOD
}

class DiscountCondition(
  val type: DiscountConditionType,

  val sequence: Int,

  val dayOfWeek: DayOfWeek,
  val startTime: LocalTime,
  val endTime: LocalTime
)

class Screening(
  val movie: Movie,
  val sequence: Int,
  val whenScreened: LocalDateTime
)

class Reservation(
  val customer: Customer,
  val screening: Screening,
  val fee: Money,
  val audienceCount: Int
)

class Customer(
  val name: String,
  val id: String
)
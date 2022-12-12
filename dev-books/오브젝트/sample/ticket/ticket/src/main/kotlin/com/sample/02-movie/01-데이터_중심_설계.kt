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
  val discountAmount: Money,
  val discountPercent: Double
)

enum class MovieType {
  AMOUNT_DISCOUNT,
  PERCENT_DISCOUNT,
  NONE_DISCOUNT
}

class Money(private val amount: BigDecimal) {

  fun plus(amount: Money): Money {
    return Money(this.amount.add(amount.amount))
  }

  fun minus(amount: Money): Money {
    return Money(this.amount.subtract(amount.amount))
  }

  fun times(percent: Double): Money {
    return Money(this.amount.multiply(BigDecimal.valueOf(percent)))
  }

  fun times(percent: Int): Money {
    return Money(this.amount.multiply(BigDecimal(percent)))
  }

  fun times(percent: Long): Money {
    return Money(this.amount.multiply(BigDecimal(percent)))
  }

  fun isLessThan(other: Money): Boolean {
    return amount < other.amount
  }

  fun isGreaterThanOrEqual(other: Money): Boolean {
    return amount >= other.amount
  }

  override fun toString(): String {
    return "Money(amount=$amount)"
  }

  companion object {
    val ZERO = wons(0)

    fun wons(amount: Long): Money {
      return Money(BigDecimal.valueOf(amount))
    }

    fun wons(amount: Double): Money {
      return Money(BigDecimal.valueOf(amount))
    }
  }


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

class ReservationAgency {

  fun reserve(screening: Screening, customer: Customer, audienceCount: Int): Reservation {
    val movie = screening.movie

    var discountable = false
    movie.discountConditions.forEach { discountCondition ->
      if(discountCondition.type == DiscountConditionType.PERIOD) {
        discountable = (screening.whenScreened.dayOfWeek == discountCondition.dayOfWeek)
          && discountCondition.startTime <=  screening.whenScreened.toLocalTime()
          && discountCondition.endTime >= screening.whenScreened.toLocalTime()
      } else {
        discountable = discountCondition.sequence == screening.sequence
      }

      if(discountable)
        return@forEach
    }

    val fee = if(discountable) {
      val discountAmount = when(movie.movieType) {
        MovieType.AMOUNT_DISCOUNT -> {
          movie.discountAmount
        }
        MovieType.PERCENT_DISCOUNT -> {
          movie.fee.times(movie.discountPercent)
        }
        MovieType.NONE_DISCOUNT -> {
          Money.ZERO
        }
      }
      movie.fee.minus(discountAmount)
    } else {
      movie.fee
    }

    return Reservation(customer, screening, fee, audienceCount)
  }
}
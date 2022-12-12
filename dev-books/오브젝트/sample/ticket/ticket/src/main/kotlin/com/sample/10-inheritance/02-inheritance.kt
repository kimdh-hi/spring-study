package com.sample.`10-inheritance`

import com.sample.`02-movie`.Money
import java.time.Duration
import java.time.LocalDateTime

class Call(
  private val from: LocalDateTime,
  private val to: LocalDateTime
) {
  fun getDuration(): Duration {
    return Duration.between(from, to)
  }

  fun getFrom() = from
}

/**
 * [차이에 의한 프로그래밍]
 * 공통의 메서드 추출
 *
 * 중복 코드를 부모 클래스로 이동
 */
abstract class Phone(
  private val calls: MutableList<Call> = mutableListOf()
) {

  fun calculateFee(): Money {
    var result = Money.ZERO

    calls.forEach {
      result = result.plus(calculateFee(it))
    }

    return result
  }

  fun call(call: Call) = calls.add(call)

  fun getCalls() = calls

  abstract fun calculateFee(call: Call): Money
}

class RegularPhone(
  private val amount: Money,
  private val seconds: Duration,
): Phone() {

  fun getAmounts() = amount

  fun getSencords() = seconds

  override fun calculateFee(call: Call): Money {
    return amount.times(call.getDuration().seconds / seconds.seconds)
  }
}

class NightlyDiscountPhone(
  private val nightlyAmount: Money,
  private val regularAmount: Money,
  private val seconds: Duration,
): Phone() {
  companion object {
    const val LATE_NIGHT_HOUR = 22
  }

  fun getNightlyAmount() = nightlyAmount

  fun getRegularAmount() = regularAmount

  fun getSencords() = seconds

  override fun calculateFee(call: Call): Money {
    return if (call.getFrom().hour >= LATE_NIGHT_HOUR) {
      nightlyAmount.plus(nightlyAmount.times(call.getDuration().seconds / seconds.seconds))
    } else {
      nightlyAmount.plus(regularAmount.times(call.getDuration().seconds / seconds.seconds))
    }
  }
}

fun main() {
  // 10초당 5원씩 부과되는 요금제
  val phone = RegularPhone(Money.wons(5), Duration.ofSeconds(10))
  phone.call(
    Call(LocalDateTime.of(2022, 12, 12, 12, 10, 0),
      LocalDateTime.of(2022, 12, 12, 12, 11, 0)))
  phone.call(
    Call(LocalDateTime.of(2022, 12, 13, 12, 10, 0),
      LocalDateTime.of(2022, 12, 13, 12, 11, 0)))

  println(phone.calculateFee())
}
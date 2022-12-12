//package com.sample.`10-inheritance`
//
//import com.sample.`02-movie`.Money
//import java.time.Duration
//import java.time.LocalDateTime
//
//class Call(
//  private val from: LocalDateTime,
//  private val to: LocalDateTime
//) {
//  fun getDuration(): Duration {
//    return Duration.between(from, to)
//  }
//
//  fun getFrom() = from
//}
//
//class Phone(
//  private val amount: Money,
//  private val seconds: Duration,
//  private val calls: MutableList<Call> = mutableListOf()
//) {
//  fun call(call: Call) = calls.add(call)
//
//  fun getCalls() = calls
//
//  fun getAmounts() = amount
//
//  fun getSencords() = seconds
//
//  fun calculateFee(): Money {
//    var result = Money.ZERO
//
//    calls.forEach {
//      result = result.plus(amount.times(it.getDuration().seconds / seconds.seconds))
//    }
//
//    return result
//  }
//}
//
//class NightlyDiscountPhone(
//  private val nightlyAmount: Money,
//  private val regularAmount: Money,
//  val seconds: Duration,
//  val calls: MutableList<Call> = mutableListOf()
//) {
//  companion object {
//    const val LATE_NIGHT_HOUR = 22
//  }
//
//  fun calculateFee(): Money {
//    var result = Money.ZERO
//
//    calls.forEach {
//      if (it.getFrom().hour >= LATE_NIGHT_HOUR) {
//        result = result.plus(nightlyAmount.times(it.getDuration().seconds / seconds.seconds))
//      } else {
//        result = result.plus(regularAmount.times(it.getDuration().seconds / seconds.seconds))
//      }
//    }
//
//    return result
//  }
//}
//
//fun main() {
//  // 10초당 5원씩 부과되는 요금제
//  val phone = Phone(Money.wons(5), Duration.ofSeconds(10))
//  phone.call(
//    Call(LocalDateTime.of(2022, 12, 12, 12, 10, 0),
//      LocalDateTime.of(2022, 12, 12, 12, 11, 0)))
//  phone.call(
//    Call(LocalDateTime.of(2022, 12, 13, 12, 10, 0),
//      LocalDateTime.of(2022, 12, 13, 12, 11, 0)))
//
//  println(phone.calculateFee())
//}
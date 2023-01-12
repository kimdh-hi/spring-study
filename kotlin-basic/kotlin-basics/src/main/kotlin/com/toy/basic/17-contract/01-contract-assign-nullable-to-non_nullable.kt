package com.toy.basic.`17-contract`

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * contract - 컴파일러에 파라미터로 전달된 값에 대한 처리를 알리는 용도 ex) 이 파라미터는 nullable 이 아니다. 등..
 */

@ExperimentalContracts
fun main() {
  val nullableData: String? = "data"
//  val nonNullableData: String = nullableData // non-nullable 에 nullable 할당시 컴파일 에러

  if(isNotNull(nullableData)) { // 여기를 지나는 순간 nullableData 는 non-nullable 로 취급 가능
    val nonNullableData: String = nullableData // non-nullable 에 nullable 할당 ok
  }

  if(nullableData.isNonNullableData()) {
    val nonNullableData: String = nullableData
  }
}

@ExperimentalContracts
fun isNotNull(value: Any?): Boolean {
  contract {
    returns(true) implies (value != null)
  }

  return false
}

@ExperimentalContracts
fun Any?.isNonNullableData(): Boolean {
  contract {
    returns(true) implies (this@isNonNullableData != null)
  }

  return false
}
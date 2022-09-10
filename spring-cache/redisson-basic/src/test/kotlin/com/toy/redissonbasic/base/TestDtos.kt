package com.toy.redissonbasic.base

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.toy.redissonbasic.common.NoArg

//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@NoArg
data class Student(
  val name: String,
  val age: Int,
  val city: String,
  val testList: List<Int>
) {
  companion object {
    fun test(): Student {
      return Student(name = "testName", age = 20, city = "testCity", testList = listOf(1,2,3,4,5))
    }
  }
}
package com.toy.springjunitmultithread.base

data class MyCounter(
  var count: Int = 0
) {
  fun increment() {
    count++
  }
}
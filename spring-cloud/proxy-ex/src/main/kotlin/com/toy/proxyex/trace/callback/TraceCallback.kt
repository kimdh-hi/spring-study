package com.study.springcoreadvanced.trace.callback

fun interface TraceCallback<T> {
  fun call(): T
}
package com.toy.springcoreadvancedaop.trace.callback

fun interface TraceCallback<T> {
  fun call(): T
}
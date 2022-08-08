package com.toy.proxyex.trace.callback

fun interface TraceCallback<T> {
  fun call(): T
}
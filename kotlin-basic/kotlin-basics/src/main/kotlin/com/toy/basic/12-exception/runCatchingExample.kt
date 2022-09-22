package com.toy.basic.`12-exception`

fun throwExceptionOrHello(input: String): String {
  if (input == "ex")
    throw RuntimeException("error...")
  return "hello"
}

fun main() {
  val result1 = try {
    throwExceptionOrHello("ex")
  } catch (ex: RuntimeException) {
    ex.message
  }
  println(result1)

  val result2 = runCatching { throwExceptionOrHello("ex") }.getOrElse { it.message }
  println(result2)
}
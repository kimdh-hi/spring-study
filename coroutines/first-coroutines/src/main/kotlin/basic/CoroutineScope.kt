package basic

import kotlinx.coroutines.*

fun main() = runBlocking {
  time()
}

suspend fun time() = coroutineScope {
  launch {
    delay(1000L)
    println("launch1")
  }

  launch {
    delay(1000L)
    println("launch2")
  }

  println("Done..")
}
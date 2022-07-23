package basic

import kotlinx.coroutines.*

fun main() = runBlocking {
  doWorld()
}

suspend fun doWorld() = coroutineScope {  // this: CoroutineScope
  launch {
    delay(1000L)
    println("World!")
  }
  println("Hello")
}
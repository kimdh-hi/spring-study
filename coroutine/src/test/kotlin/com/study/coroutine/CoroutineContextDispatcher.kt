package com.study.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
  // runBlocking 의 context 사용 (main thread)
  launch {
    println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
  }

  launch(Dispatchers.Default) {
    println("Default               : I'm working in thread ${Thread.currentThread().name}")
  }

  launch(Dispatchers.IO) {
    println("IO                    : I'm working in thread ${Thread.currentThread().name}")
  }
}

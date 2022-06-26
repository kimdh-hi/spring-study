package com.toy.`coroutines-basic`.`04-dispatchers`

import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
  launch {
    println("main runBlocking, thread-name: ${Thread.currentThread().name}")
  }

  launch(Unconfined) {
    println("Unconfined, thread-name: ${Thread.currentThread().name}")
  }

  launch(Default) {
    println("Default, thread-name: ${Thread.currentThread().name}")
  }

  launch(newSingleThreadContext("MyThread")) {
    println("newSingleThreadContext, thread-name: ${Thread.currentThread().name}")
  }
}
package com.toy.`coroutines-basic`.`04-dispatchers`

import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

/**
 * Dispatchers <-> Thread ...
 *
 * Dispatcher...
 * - 코루틴이 어떤 Thread 에서 실행
 * - lanuch, async 등 모든 CoroutineBuilder 는 optional 로 CoroutineContext 를 인자로 받는다
 *   어떤 CoroutineContext ==> 어떤 Dispatcher 를 사용할지에 대한 설정
 *
 *
 */

fun main(): Unit = runBlocking {
  // 부모 (여기선 runBlocking) 의 CoroutineContext를 그대로 사용
  // Main Thread
  launch {
    println("main runBlocking, thread-name: ${Thread.currentThread().name}")
  }

  launch(IO) {
    println("IO, thread-name: ${Thread.currentThread().name}")
  }

  // Main Thread
  launch(Unconfined) {
    println("Unconfined, thread-name: ${Thread.currentThread().name}")
  }

  // DefaultDispatcher == GlobalScope
  launch(Default) {
    println("Default, thread-name: ${Thread.currentThread().name}")
  }

  // 비용이 높은 방식 (bad ...)
  // 코루틴을 실행할 때마다 명시적으로 새로운 쓰레드를 셍성한다
  launch(newSingleThreadContext("MyThread")) {
    println("newSingleThreadContext, thread-name: ${Thread.currentThread().name}")
  }

  // newSingleThreadContext 로 코루틴을 생성해서 사용하려면 use와 함께 사용하는 것이 좋음
  // 새로 생성된 쓰레드에 대한 close 가 필요
  newSingleThreadContext("MyThread-2").use {
    launch(it) {
      println("newSingleThreadContextV2, thread-name: ${Thread.currentThread().name}")
    }
  }

}
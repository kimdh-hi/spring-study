package com.toy.`coroutines-basic`.`10-withContext`

import kotlinx.coroutines.*

val exceptionHandler3 = CoroutineExceptionHandler { coroutineContext, throwable ->
  println("exceptionHandler3")
}

// supervisorJob 은 에러의 전파방향을 자기자신(자식) 으로 한정짓기 위해 사용한다. (전파X)
// 보통 coroutineContext 와 + 연산자로 결합하여 사용한다.
val supervisorJob = SupervisorJob()

// async 블럭에서 try-catch 시 catch 쪽에서 예외처리를 끝내고 싶다면 supervisorJob을 사용
// supervisorJob 사용시 exceptionHandler 에 예외가 걸리지 않고 catch 블럭에 걸리게 됨.
suspend fun main(): Unit = coroutineScope {
  launch(exceptionHandler3) {
    try {
      async(Dispatchers.IO + supervisorJob) {
        throw RuntimeException("ex...")
      }.await()
    } catch (e: RuntimeException) {
      println("catch e...")
    }
  }
}
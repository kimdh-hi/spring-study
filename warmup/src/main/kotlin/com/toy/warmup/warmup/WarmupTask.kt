package com.toy.warmup.warmup

interface WarmupTask {

  val repeat: Int

  fun run(iteration: Int)
}

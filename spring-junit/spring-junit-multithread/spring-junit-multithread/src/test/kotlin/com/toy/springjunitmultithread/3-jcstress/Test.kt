package com.toy.springjunitmultithread.`3-jcstress`

import com.toy.springjunitmultithread.base.MyCounter
import org.junit.Test
import org.openjdk.jcstress.annotations.*

@JCStressTest
@Outcome(id = ["1"], expect = Expect.ACCEPTABLE, desc = "One update lost.")
@Outcome(id = ["2"], expect = Expect.ACCEPTABLE, desc = "Both updates.")
@State
class Test {

  private val counter = MyCounter()

  @Actor
  fun actor1() {
    counter.increment()
  }

  @Actor
  fun actor2() {
    counter.increment()
  }

  @Arbiter
  @Test
  fun arbiter() {
    println(counter.count)
  }
}
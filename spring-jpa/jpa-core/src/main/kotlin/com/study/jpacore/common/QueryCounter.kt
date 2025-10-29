package com.study.jpacore.common

object QueryCounter {
  private val counter = ThreadLocal.withInitial { 0 }

  fun increase() {
    counter.set(counter.get() + 1)
  }

  fun get(): Int = counter.get()

  fun set(count: Int) = counter.set(count)

  fun needLogging() = get() > 0

  fun clear() = counter.remove()
}

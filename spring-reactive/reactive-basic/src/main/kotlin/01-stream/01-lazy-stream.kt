package `01-stream`

import java.util.stream.Stream

fun main() {
  val stream = Stream.of(1)
    .map {
      Thread.sleep(1000L)
      it * 2
    }

  //nothing happen... (lazy)
  println(stream)

  stream.forEach { println(it) }
}
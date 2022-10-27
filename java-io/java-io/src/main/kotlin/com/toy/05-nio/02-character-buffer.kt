package com.toy.`05-nio`

import java.nio.BufferUnderflowException
import java.nio.CharBuffer
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

fun main() {
  val text = """
    test
    abc
    hello...
  """.trimIndent()
  val charBuffer = CharBuffer.allocate(1024)
    .put(text)
    .flip()

  val byteBuffer = StandardCharsets.UTF_8
    .encode(charBuffer)

  val path = Paths.get("src/main/kotlin/com/toy/05-nio/char-test.bin")
  // 파일 create -> write
  FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE).use {
    it.write(byteBuffer)
  }

  // read
  FileChannel.open(path, StandardOpenOption.READ).use {
    byteBuffer.clear()
    it.read(byteBuffer)
    byteBuffer.flip()
    val decodedBuffer = StandardCharsets.UTF_8
      .decode(byteBuffer)
    while (true) {
      try {
        print(decodedBuffer.get())
      } catch (e: BufferUnderflowException) {
        break
      }
    }
  }
}
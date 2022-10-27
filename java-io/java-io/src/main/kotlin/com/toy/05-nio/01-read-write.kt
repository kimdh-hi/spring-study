package com.toy.`05-nio`

import java.io.File
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * 1. 버퍼생성 ByteBuffer.allocate
 * 2. 미사용 버퍼 정리 byteBuffer.flip()
 * 3. FileChannel.open(path, StandardOpenOption...)
 * 4. FileChannel.write(buffer) or FileChannel.read(buffer)
 */
fun main() {
  val byteBuffer = ByteBuffer.allocate(1024) // 1024kb
  byteBuffer.putInt(123) // 4
  byteBuffer.putInt(456) // 4
  println(byteBuffer.position()) // 4 + 4 = 8
  println(byteBuffer.limit()) // 1024

  byteBuffer.flip()
  println(byteBuffer.position()) // 4 + 4 = 8
  println(byteBuffer.limit()) // 0

  val path = Paths.get("src/main/kotlin/com/toy/05-nio/test.bin")
  val fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE)
  fileChannel.write(byteBuffer)
  fileChannel.close()

  val readFileChannel = FileChannel.open(path, StandardOpenOption.READ)
  byteBuffer.clear()
  readFileChannel.read(byteBuffer)
  readFileChannel.close()
  byteBuffer.flip()
  val intBuffer = byteBuffer.asIntBuffer()

    while(true) {
      try {
        println(intBuffer.get())
      } catch (e: BufferUnderflowException) {
        break
      }
    }
}
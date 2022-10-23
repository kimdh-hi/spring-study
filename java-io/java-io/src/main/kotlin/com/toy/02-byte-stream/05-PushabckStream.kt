package com.toy.`02-byte-stream`

import java.io.ByteArrayInputStream
import java.io.PushbackInputStream

fun main() {

  val inputData = "if(a && b) return c & d;"
  val byteArray = inputData.toByteArray()

  ByteArrayInputStream(byteArray).use { inputStream ->
    PushbackInputStream(inputStream).use { pushBackInputStream ->
      while (true) {
        val read = pushBackInputStream.read()
        if (read == -1)
          break
        when (read.toChar()) {
          '&' -> {
            val nextRead = pushBackInputStream.read()
            if (nextRead.toChar() == '&')
              print("[Logical-And]")
            else {
              print("[Bitwise-And]")
              // &가 연속되는 경우 연속되는 두번째 &는 무시한다. (두번째 & 는 읽지 않은 것 처럼.)
              pushBackInputStream.unread(nextRead)
            }
          }

          else -> {
            print(read.toChar())
          }
        }
      }
    }
  }
}
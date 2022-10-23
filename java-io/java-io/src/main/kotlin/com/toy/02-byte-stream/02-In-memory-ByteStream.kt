package com.toy.`02-byte-stream`

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

fun main() {
  val message1 = "12345"
  val bytes1: ByteArray = message1.toByteArray()
  val inputStream = ByteArrayInputStream(bytes1)
  while(true) {
    val readByte = inputStream.read(ByteArray(3)) // 3byte 씩 read
    if(readByte == -1)
      break
    println(readByte)
  }
  inputStream.close()

  println("=======================")

  val message2 = "김대현"
  val bytes2 = message2.toByteArray()
  val inputStream2 = ByteArrayInputStream(bytes2, 6, 3) // 6byte 이후부터 3byte 만큼
  while(true) {
    val readByte = inputStream2.read(ByteArray(3))
    if(readByte == -1)
      break
    println(readByte)
  }
  inputStream2.close()

  println("=======================")

  val message3 = "abcdef"
  val bytes3 = message3.toByteArray()
  val inputStream3 = ByteArrayInputStream(bytes3)
  val outputStream = ByteArrayOutputStream()
  while(true) {
    val readByte = inputStream3.read(ByteArray(1))
    if (readByte == -1)
      break
    outputStream.write(readByte)
  }
  outputStream.toByteArray().forEach {
    println(it)
  }

  inputStream3.close()
  outputStream.close()

}
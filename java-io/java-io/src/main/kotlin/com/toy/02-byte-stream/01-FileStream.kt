package com.toy.`02-byte-stream`

import java.io.FileInputStream

fun main() {

  val fis = FileInputStream("/Users/daehyunkim/Documents/study/java-io/dir1/test1.txt")

  while (true) {
    val readByte = fis.read()
    if(readByte == -1)
      break
    print(readByte)
  }

  fis.close()
}
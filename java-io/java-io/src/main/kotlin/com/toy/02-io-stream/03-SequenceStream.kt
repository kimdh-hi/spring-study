package com.toy.`02-io-stream`

import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.SequenceInputStream

fun main() {
  val file1 = File("/Users/daehyunkim/Documents/study/java-io/dir1/test1.txt")
  val file2 = File("/Users/daehyunkim/Documents/study/java-io/dir1/test2.txt")

  val fis1 = FileInputStream(file1)
  val fis2 = FileInputStream(file2)

  val sequenceInputStream = SequenceInputStream(fis1, fis2)
  while(true) {
    val read = sequenceInputStream.read()
    if(read == -1)
      break
    print(read.toChar())
  }
  fis1.close()
  fis2.close()
  sequenceInputStream.close()

  println()
  println("=============================")

  val data = "data"
  val fis3 = FileInputStream(file1)
  val fis4 = FileInputStream(file2)
  val bis = ByteArrayInputStream(data.toByteArray())
  val sequenceInputStream2 = SequenceInputStream(SequenceInputStream(fis3, fis4), bis)
  while (true) {
    val read = sequenceInputStream2.read()
    if (read == -1)
      break
    print(read.toChar())
  }
  fis3.close()
  fis4.close()
  bis.close()
  sequenceInputStream2.close()
}
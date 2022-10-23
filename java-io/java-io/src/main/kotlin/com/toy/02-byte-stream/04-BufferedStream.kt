package com.toy.`02-byte-stream`

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

/**
 * BuffedStream 은 메모리 버퍼를 통해 성능향을 가능토록 한다.
 * 바로 디스크에 반영하지 않고 메모리 버퍼의 크기(default: 8192)만큼 데이터를 쌓고 한번에 디스크에 반영한다.
 */
fun main() {
  val file1 = File("/Users/daehyunkim/Documents/study/java-io/dir1/test1.txt")
  FileInputStream(file1).use { fis ->
    BufferedInputStream(fis).use { bis ->
      while(true) {
        val read = bis.read()
        if(read == -1)
          break
        print(read.toChar())
      }
    }
  }
}
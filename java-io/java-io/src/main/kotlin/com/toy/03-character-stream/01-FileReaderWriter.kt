package com.toy.`03-character-stream`

import java.io.File
import java.io.FileReader
import java.io.FileWriter

fun main() {
  val file = File("src/test.txt")
  val outFile = File("src/test-write.txt")
  FileReader(file).use { reader ->
    FileWriter(outFile).use { writer ->
      while(true) {
        val read = reader.read()
        if(read == -1)
          break
        if(read.toChar() == '*')
          writer.write(read)
        print(read.toChar())
      }
    }
  }
}
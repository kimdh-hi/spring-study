package com.toy.`03-character-stream`

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

fun main() {
  val file = File("src/test.txt")
  val outFile = File("src/test-write.txt")

  BufferedReader(FileReader(file)).use { reader ->
    BufferedWriter(FileWriter(outFile)).use { writer ->
      reader.lines()
        .map { it.uppercase() }
        .forEach { line ->
          writer.write(line)
          writer.newLine()
          println(line)
        }
    }
  }
  //  BufferedReader(FileReader(file)).use { reader ->
//    BufferedWriter(FileWriter(outFile)).use { writer ->
//      while (true) {
//        val line = reader.readLine() ?: break
//        writer.write(line)
//        writer.newLine()
//        println(line)
//      }
//    }
//  }
}

package com.toy.basic.`05-checked-exception`

import java.io.File
import java.io.FileOutputStream

fun main() {
 val file = File("test.txt")
  FileOutputStream(file).use {
    it.write("test".toByteArray())
  }
}



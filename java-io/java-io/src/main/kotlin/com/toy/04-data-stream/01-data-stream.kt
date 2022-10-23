package com.toy.`04-data-stream`

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.EOFException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun main() {
  val file = File("src/main/kotlin/com/toy/04-data-stream/data-stream-write-test.txt")
  file.createNewFile()

  val items = arrayOf("itema", "itemb", "itemc")
  val costs = arrayOf<Long>(1500, 200, 5000)

  FileOutputStream(file).use { fout ->
    DataOutputStream(fout).use { dout ->
      for (i in items.indices) {
        dout.writeInt(i)
        dout.writeUTF(items[i])
        dout.writeLong(costs[i])
      }
    }
  }

  FileInputStream(file).use { fin ->
    DataInputStream(fin).use { din ->
      var total: Long = 0
      try {
        while (true) {
          val idx = din.readLong()
          val item = din.readUTF()
          val cost = din.readLong()
          total += cost
          println("$idx $item: $cost")
        }
        println("total: $total")
      } catch(e: EOFException) {}
    }
  }
}
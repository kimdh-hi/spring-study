package com.toy.`01-file`

import java.io.File

fun main() {
  val dir1 = File("/Users/daehyunkim/Documents/study/java-io/dir1")
  val dir2 = File("/Users/daehyunkim/Documents/study/java-io/dir1/dir2")
  val dir3 = File("/Users/daehyunkim/Documents/study/java-io/dir1/dir3")
  val dir4 = File("/Users/daehyunkim/Documents/study/java-io/dir1/dir2/dir4")
  val file1 = File("/Users/daehyunkim/Documents/study/java-io/dir1/test1.txt")
  val file2 = File("/Users/daehyunkim/Documents/study/java-io/dir1/test2.txt")
  val file3 = File("/Users/daehyunkim/Documents/study/java-io/dir1/dir2/test.txt")

  dir1.mkdir()
  dir2.mkdir()
  dir3.mkdir()
  dir4.mkdir()
  file1.createNewFile()
  file2.createNewFile()
  file3.createNewFile()
//  println(dir1.exists())
//  println(dir2.exists())
//  println(dir3.exists())
//  println(dir4.exists())
//  println(file1.exists())
//  println(file2.exists())
//  println(file3.exists())

  dir1.list()?.forEach {
    if(File(dir1, it).isFile)
      println("isFile: $it")
    else if (File(dir1, it).isDirectory)
      println("isDirectory: $it")
  }

  var numberOfFiles = 0
  var numberOfDirs = 0

  dir1.list()?.forEach {
    if(File(dir1,it).isFile)
      numberOfFiles++
    else if (File(dir1, it).isDirectory)
      numberOfDirs++
  }
  println("numberOfFiles: $numberOfFiles, numberOfDirs: $numberOfDirs")

  println("=========path==========")
  val pathTestFile = File("/Users/daehyunkim/Documents/study/java-io/dir1/dir2/../test-txt")
  pathTestFile.createNewFile()
  println(pathTestFile.path) // /Users/daehyunkim/Documents/study/java-io/dir1/dir2/../test-txt
  println(pathTestFile.absolutePath) // /Users/daehyunkim/Documents/study/java-io/dir1/dir2/../test-txt
  println(pathTestFile.canonicalPath) // /Users/daehyunkim/Documents/study/java-io/dir1/test-txt => 상대경로가 적용된 path
}
package com.toy.`06-nio2`

import java.net.URI
import java.nio.file.spi.FileSystemProvider

fun main() {
  val providers = FileSystemProvider.installedProviders()
  providers.forEach {
    println(it)
  }

  val defaultFileSystem = providers[0].getFileSystem(URI.create("file:///"))
  println(defaultFileSystem)
}
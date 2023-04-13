package com.toy.springaws.helper

import org.slf4j.LoggerFactory
import org.springframework.util.FileCopyUtils
import java.io.File

class LocalUploadHelper: UploaderHelper() {
  private val log = LoggerFactory.getLogger(javaClass)

  override fun upload(file: File, path: String) {
    try {
      val fullPath = getFullPath(path)
      val destFile = File(fullPath)

      FileCopyUtils.copy(file, destFile)
    } catch (e: NoSuchFileException) {
      log.error(e.message, e)
      removeTempFile(file)
    }
  }

  private fun getFullPath(path: String): String {
    val fullPath = "/src/main/resources$path"
    mkdir(fullPath)
    return fullPath
  }

  private fun mkdir(dir: String) {
    val file = File(dir).parentFile
    if (!file.exists())
      file.mkdirs()
  }

  override fun delete(path: String) {
    val file = File(path)
    if (file.exists())
      file.delete()
  }
}
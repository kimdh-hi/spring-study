package com.toy.springaws.helper

import org.slf4j.LoggerFactory
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

abstract class UploaderHelper {

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    const val UPLOAD_URI = "/upload"
  }

  protected abstract fun upload(file: File, path: String)

  fun upload(multipartFile: MultipartFile, prefix: String): UploadResult {
    val file = convert(multipartFile) ?: throw RuntimeException("...")
    val path = getPath(file, prefix)
    log.debug("upload path: $path")

    upload(file, path)
    removeTempFile(file)

    return UploadResult(path)
  }

  private fun getPath(file: File, prefix: String): String {
    val prefix = generatePrefix()
    val fileName = generateFileName(file)
    return "$UPLOAD_URI/${prefix}/$prefix/$fileName"
  }

  private fun generatePrefix() = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

  private fun generateFileName(file: File): String {
    val uuid = UUID.randomUUID().toString()
    val extension = StringUtils.getFilenameExtension(file.name)
    return "$uuid.$extension"
  }

  private fun convert(multipartFile: MultipartFile): File? {
    val file = multipartFile.originalFilename?.let { File(it) } ?: return null
    return if (file.createNewFile()) {
      FileOutputStream(file).use { fos -> fos.write(multipartFile.bytes) }
      file
    } else {
      null
    }
  }

  protected fun removeTempFile(file: File) {
    if (file.exists() && !file.delete())
      log.warn("File[${file.canonicalPath}] delete fail..")
  }
}

data class UploadResult(
  val path: String
)
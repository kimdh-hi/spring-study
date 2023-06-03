package com.toy.springmvc.controller

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.zip.ZipInputStream

@RequestMapping("/unzip")
@RestController
class UnzipController {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
  fun unzip(request: UnzipRequest) {

    try {
      request.zipFile.inputStream.use { inputStream ->
        ZipInputStream(inputStream).use { zipInput ->
          generateSequence { zipInput.nextEntry }.forEach { zipEntry ->
            val unzipFile = File(zipEntry.name)
            log.info("unzipFile {}", unzipFile.name)
          }
          zipInput.closeEntry()
        }
      }
    } catch (e: Exception) {
      log.error("unzip failed...")
    }
  }
}

data class UnzipRequest(
  val zipFile: MultipartFile
)
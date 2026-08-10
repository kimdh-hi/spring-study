package com.study.presignedurl.ui

import com.study.presignedurl.application.FileService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/files")
class FileController(private val fileService: FileService) {

  @PostMapping("/upload-url")
  fun uploadUrl(@RequestBody request: UploadUrlRequest): UploadUrlResponse {
    val (id, url) = fileService.issueUploadUrl(request.originalName, request.contentType)
    return UploadUrlResponse(id, url)
  }

  @PostMapping("/multipart-url")
  fun multipartUrl(@RequestBody request: MultipartUrlRequest): MultipartUrlResponse {
    val (id, uploadId, urls) =
      fileService.issueMultipartUrls(request.originalName, request.contentType, request.partCount)
    return MultipartUrlResponse(id, uploadId, urls)
  }

  @PostMapping("/{id}/multipart-complete")
  fun multipartComplete(@PathVariable id: Long, @RequestBody request: MultipartCompleteRequest) =
    fileService.completeMultipart(id, request.uploadId, request.parts.map { it.partNumber to it.etag })

  @PostMapping("/{id}/complete")
  fun complete(@PathVariable id: Long) = fileService.completeUpload(id)

  @PostMapping("/{id}/download-url")
  fun downloadUrl(@PathVariable id: Long) = DownloadUrlResponse(fileService.createDownloadUrl(id))
}

data class UploadUrlRequest(val originalName: String, val contentType: String)

data class UploadUrlResponse(val id: Long, val url: String)

data class DownloadUrlResponse(val url: String)

data class MultipartUrlRequest(
  val originalName: String,
  val contentType: String,
  val partCount: Int,
)

data class MultipartUrlResponse(val id: Long, val uploadId: String, val urls: List<String>)

data class MultipartCompleteRequest(val uploadId: String, val parts: List<PartRequest>)

data class PartRequest(val partNumber: Int, val etag: String)

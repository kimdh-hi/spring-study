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

  @PostMapping("/{id}/complete")
  fun complete(@PathVariable id: Long) = fileService.completeUpload(id)

  @PostMapping("/{id}/download-url")
  fun downloadUrl(@PathVariable id: Long) = DownloadUrlResponse(fileService.createDownloadUrl(id))
}

data class UploadUrlRequest(val originalName: String, val contentType: String)

data class UploadUrlResponse(val id: Long, val url: String)

data class DownloadUrlResponse(val url: String)

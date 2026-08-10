package com.study.presignedurl.application

import com.study.presignedurl.domain.model.FileObject
import com.study.presignedurl.domain.repository.FileObjectRepository
import com.study.presignedurl.infra.S3Storage
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FileService(
  private val fileObjectRepository: FileObjectRepository,
  private val s3Storage: S3Storage,
) {
  fun issueUploadUrl(originalName: String, contentType: String): Pair<Long, String> {
    val file = fileObjectRepository.save(FileObject(originalName, contentType))
    return file.id to s3Storage.uploadUrl(file.objectKey)
  }

  fun issueMultipartUrls(
    originalName: String,
    contentType: String,
    partCount: Int,
  ): Triple<Long, String, List<String>> {
    val file = fileObjectRepository.save(FileObject(originalName, contentType))
    val uploadId = s3Storage.startMultipart(file.objectKey, contentType)
    return Triple(file.id, uploadId, s3Storage.partUrls(file.objectKey, uploadId, partCount))
  }

  fun completeMultipart(id: Long, uploadId: String, parts: List<Pair<Int, String>>) {
    val file = find(id)
    s3Storage.completeMultipart(file.objectKey, uploadId, parts)
    file.markUploaded()
  }

  fun completeUpload(id: Long) {
    val file = find(id)
    require(s3Storage.exists(file.objectKey)) { "업로드되지 않은 파일: $id" }
    file.markUploaded()
  }

  @Transactional(readOnly = true)
  fun createDownloadUrl(id: Long): String {
    val file = find(id)
    require(file.uploaded) { "업로드 완료되지 않은 파일: $id" }
    return s3Storage.downloadUrl(file.objectKey)
  }

  private fun find(id: Long) = fileObjectRepository.findById(id)
    .orElseThrow { IllegalArgumentException("존재하지 않는 파일: $id") }
}

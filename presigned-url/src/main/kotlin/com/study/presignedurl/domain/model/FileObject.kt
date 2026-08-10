package com.study.presignedurl.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "file_objects")
class FileObject(
  val originalName: String,
  val contentType: String,
  val objectKey: String = "uploads/${UUID.randomUUID()}",
  var uploaded: Boolean = false,
  val createdAt: Instant = Instant.now(),
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,
) {
  fun markUploaded() {
    uploaded = true
  }
}

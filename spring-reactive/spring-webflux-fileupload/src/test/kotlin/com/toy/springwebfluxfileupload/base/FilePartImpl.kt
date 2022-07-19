package com.toy.springwebfluxfileupload.base

import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.codec.multipart.FilePart
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.FileInputStream
import java.nio.file.Path

class FilePartImpl(
  val name: String,
  val fis: FileInputStream
): FilePart {

  companion object {
    val factory = DefaultDataBufferFactory()
  }

  override fun name(): String = name

  override fun headers(): HttpHeaders = HttpHeaders.EMPTY

  override fun content(): Flux<DataBuffer> = DataBufferUtils.read(
    ByteArrayResource(fis.readAllBytes()), factory, 1024)

  override fun filename(): String = name

  override fun transferTo(dest: Path): Mono<Void> {
    return Mono.empty()
  }
}
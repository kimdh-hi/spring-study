package com.toy.springwebfluxfileupload.controller

import com.toy.springwebfluxfileupload.base.FilePartImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.io.FileInputStream


@SpringBootTest
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class FileControllerTest(
  val webClient: WebTestClient
) {

  @Test
  fun file(): Unit = runBlocking {
    //given
    val fis = withContext(Dispatchers.IO) {
      FileInputStream("src/test/resources/images/test.png")
    }
    val filePart = FilePartImpl("file.txt", fis)

    val builder = MultipartBodyBuilder()
    builder.part("file", filePart)

    //when
    webClient.post()
      .uri("/file")
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .body(BodyInserters.fromMultipartData(builder.build()))
      .exchange()
  }
}
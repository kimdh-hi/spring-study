package com.study.presignedurl

import com.study.presignedurl.ui.DownloadUrlResponse
import com.study.presignedurl.ui.UploadUrlRequest
import com.study.presignedurl.ui.UploadUrlResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestClient
import org.testcontainers.localstack.LocalStackContainer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException
import java.net.URI
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(PresignedUrlTest.LocalStackTestConfig::class)
class PresignedUrlTest {

  @TestConfiguration(proxyBeanMethods = false)
  class LocalStackTestConfig {
    @Bean
    @ServiceConnection
    fun localStackContainer(): LocalStackContainer =
      LocalStackContainer("localstack/localstack:4").withServices("s3")
  }

  @LocalServerPort
  private var port: Int = 0

  @Autowired
  private lateinit var s3Client: S3Client

  private val rest = RestClient.create()

  @BeforeEach
  fun createBucket() {
    try {
      s3Client.createBucket { it.bucket("sample-bucket") }
    } catch (_: BucketAlreadyOwnedByYouException) {
    }
  }

  @Test
  fun `발급받은 URL로 업로드하고 다시 발급받은 URL로 내려받는다`() {
    val issued = issueUploadUrl("hello.txt")

    rest.put().uri(URI.create(issued.url)).body("hello presigned").retrieve().toBodilessEntity()
    rest.post().uri(app("/files/${issued.id}/complete")).retrieve().toBodilessEntity()

    val download = rest.post().uri(app("/files/${issued.id}/download-url"))
      .retrieve().body(DownloadUrlResponse::class.java)!!

    val body = rest.get().uri(URI.create(download.url)).retrieve().body(String::class.java)
    assertEquals("hello presigned", body)
  }

  @Test
  fun `업로드하지 않은 파일은 완료 처리에 실패한다`() {
    val issued = issueUploadUrl("missing.txt")

    assertFailsWith<HttpServerErrorException> {
      rest.post().uri(app("/files/${issued.id}/complete")).retrieve().toBodilessEntity()
    }
  }

  private fun issueUploadUrl(name: String) = rest.post()
    .uri(app("/files/upload-url"))
    .body(UploadUrlRequest(name, "text/plain"))
    .retrieve()
    .body(UploadUrlResponse::class.java)!!

  private fun app(path: String) = "http://localhost:$port$path"
}

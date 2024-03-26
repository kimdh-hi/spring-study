package com.sample.springrestclient

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity

@SpringBootTest
class RestClientTest @Autowired constructor(
  private val restClient: RestClient
) {

  @Test
  fun get() {
    val result = restClient.get()
      .uri("https://jsonplaceholder.typicode.com/posts")
      .retrieve()
      .toEntity<List<Post>>()

    println(result.body?.size)
  }

  @Test
  fun post() {
    restClient.post()
      .uri("https://jsonplaceholder.typicode.com/posts")
      .body(Post("user1", "id1", "title1", "body1"))
      .retrieve()
      .toBodilessEntity()
  }

  @Test
  fun fakeApi() {
    restClient.get()
      .uri("http://localhost:8084/fake/exception")
      .retrieve()
      .toBodilessEntity()
  }
}

data class Post(
  val userId: String,
  val id: String,
  val title: String,
  val body: String
)
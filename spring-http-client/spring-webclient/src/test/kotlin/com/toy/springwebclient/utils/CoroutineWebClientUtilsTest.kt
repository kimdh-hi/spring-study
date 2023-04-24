package com.toy.springwebclient.utils

import com.toy.springwebclient.service.FakeUser
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import reactor.test.StepVerifier

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class CoroutineWebClientUtilsTest(
  val webClientUtils: CoroutineWebClientUtils
) {

  companion object {
    //https://fakerestapi.azurewebsites.net/index.html
    const val FAKE_REST_API_URI = "https://fakerestapi.azurewebsites.net/api/v1/Users"
  }

  @Test
  fun post() = runBlocking {
    //given
    val fakeUser = FakeUser(id = 1, userName = "test-username", password = "test-password")

    //when
    val response = webClientUtils.post(
      uri = FAKE_REST_API_URI,
      body = fakeUser,
      returnType = FakeUser::class
    ).awaitSingle()

    //then
    org.junit.jupiter.api.assertAll({
      assertNotNull(response)
      assertEquals(response.id, 1)
    })
  }

  @Test
  fun postError(): Unit = runBlocking {
    //given
    val user =  FakeUser()

    //expect
    org.junit.jupiter.api.assertThrows<RuntimeException> {
      webClientUtils.post(
        uri = FAKE_REST_API_URI,
        body = user,
        returnType = FakeUser::class
      ).awaitSingle()
    }
  }

  @Test
  fun get() = runBlocking {
    //when
    val response = webClientUtils.get(
      uri = "${FAKE_REST_API_URI}/1",
      returnType = FakeUser::class
    ).awaitSingle()

    //then
    org.junit.jupiter.api.assertAll({
      assertNotNull(response)
      assertEquals(response.id, 1)
    })
  }

  @Test
  fun getError(): Unit = runBlocking {
    org.junit.jupiter.api.assertThrows<RuntimeException> {
      webClientUtils.get(
        uri = "${FAKE_REST_API_URI}/error",
        returnType = FakeUser::class
      ).awaitSingle()
    }
  }

  @Test
  fun put() = runBlocking {
    //given
    val fakeUser = FakeUser(id = 1, userName = "test-username", password = "test-password")

    //when
    val response = webClientUtils.put(
      uri = "${FAKE_REST_API_URI}/1",
      body = fakeUser,
      returnType = FakeUser::class
    ).awaitSingle()

    //then
    org.junit.jupiter.api.assertAll({
      assertNotNull(response)
      assertEquals(response.id, 1)
    })
  }

  @Test
  fun delete() = runBlocking {
    //when
    val response = webClientUtils.delete(
      uri = "${FAKE_REST_API_URI}/1",
      returnType = Unit::class
    ).awaitSingleOrNull()

    //then
    org.junit.jupiter.api.assertAll({
      assertNull(response)
    })
  }

  @Test
  fun getFlux() = runBlocking {
    //when
    val responseFlow = webClientUtils.get(
      uri = FAKE_REST_API_URI,
      returnType = FakeUser::class
    ).asFlow()

    //then
  }
}
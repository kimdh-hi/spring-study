package com.toy.okhttp3.utils

import com.toy.okhttp3.common.NoArg
import com.toy.okhttp3.config.OkHttpProperties
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class OkHttpUtilsTest(
  val okHttpUtils: OkHttpUtils,
  val okHttpProperties: OkHttpProperties
) {

  companion object {
    const val FAKE_REST_API_URI = "https://fakerestapi.azurewebsites.net/api/v1/Users"
  }

  @Test
  fun post() {
    //given
    val fakeUser = FakeUser(id = 1, userName = "test-username", password = "test-password")

    //when
    val response = okHttpUtils.post(
      url = FAKE_REST_API_URI,
      returnType = FakeUser::class,
      body = fakeUser
    )

    //then
    println(response)
    assertNotNull(response)
  }

  @Test
  fun get() {
    //when
    val response = okHttpUtils.get(
      url = "$FAKE_REST_API_URI/1",
      returnType = FakeUser::class,
    )

    //then
    println(response)
    assertNotNull(response)
  }
}

@NoArg
data class FakeUser(
  val id: Int,
  val userName: String,
  val password: String
)
package com.toy.oauthbasic.utils

import org.springframework.util.SerializationUtils
import java.io.Serializable
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

object CookieUtils {

  fun get(request: HttpServletRequest, name: String): Cookie? {
    val cookies = request.cookies
    if (cookies != null && cookies.isNotEmpty()) {
      for (cookie in cookies) {
        if (cookie.name.equals(name)) {
          return cookie
        }
      }
    }
    return null
  }

  fun add(response: HttpServletResponse, name: String, value: String?, maxAge: Int) {
    val cookie = Cookie(name, value)
    cookie.path = "/"
    cookie.isHttpOnly = true
    cookie.maxAge = maxAge
    response.addCookie(cookie)
  }

  fun delete(request: HttpServletRequest, response: HttpServletResponse, name: String) {
    val cookies = request.cookies
    if (cookies != null && cookies.isNotEmpty()) {
      for (cookie in cookies) {
        if (cookie.name.equals(name)) {
          cookie.value = ""
          cookie.path = "/"
          cookie.maxAge = 0
          response.addCookie(cookie)
        }
      }
    }
  }

  fun serialize(obj: Any): String {
    return Base64.getUrlEncoder()
      .encodeToString(SerializationUtils.serialize(obj as Serializable))
  }

  fun <T> deserialize(cookie: Cookie, cls: Class<T>): T {
    return cls.cast(
      SerializationUtils.deserialize(
        Base64.getUrlDecoder().decode(cookie.value)
      )
    )
  }
}
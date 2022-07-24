package com.toy.reactivejdsl.common

object UrlConstants {

  //"/swagger-ui.html/**", "/webjars/swagger-ui/**", "/v3/api-docs/**"

  val SWAGGER_V3 = arrayOf(
    "/v3/api-docs/**",
    "/api-docs/**",
    "/swagger-ui/**",
    "/swagger",
    "/webjars/swagger-ui/**"
  )

  val JWT_FILTER_SKIP_URL = listOf("/api/login", "/api/users/signup")
}
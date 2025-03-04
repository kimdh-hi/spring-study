package com.toy.springfunctionalendpoint

import org.springframework.context.support.beans
import org.springframework.web.servlet.function.router

val routeBeans = beans {
  bean(::testRoute)
}

fun testRoute() = router {
  "/test".nest {
    GET("") { req -> ok().body("test! params=${req.params()}") }
    GET("/{id}") { req -> ok().body("test! id=${req.pathVariable("id")}") }
    POST { req -> ok().body("test! param=${req.body(TestRequest::class.java)}") }
  }
}

private data class TestRequest(
  val data: String,
)

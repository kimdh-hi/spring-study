package com.study.testcontainer

import org.springframework.boot.fromApplication

fun main(args: Array<String>) {
  fromApplication<TestcontainerApplication>()
    .run(*args)
}

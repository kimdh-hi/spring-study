package com.study.resilience4j.httpclient

interface TestExchange {

  fun test1(status: Int): String

  fun test2(status: Int): String
}
